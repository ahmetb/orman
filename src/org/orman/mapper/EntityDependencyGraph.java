package org.orman.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.orman.mapper.exception.UnsolvableDependencyOrderException;
import org.orman.sql.util.Glue;
import org.orman.util.logging.Log;

/**
 * Computes entity dependency graph to provide creation and deletion order for
 * given entities. Uses foreign keys, relationships between entities to compute
 * this.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 */
public class EntityDependencyGraph {

	private final class Node implements Comparable<Node> {
		protected Entity entity;
		protected Set<Entity> requires = new HashSet<Entity>();
		protected boolean checked = false;
		
		public Node(Entity e){ this.entity = e; }

		public int compareTo(Node o) {
			return new Integer(requires.size()).compareTo(o.requires.size());
		}
		
		public String toString() { return entity.getOriginalName(); }
	}

	private List<Entity> entityList;
	private LinkedList<Node> nodes = new LinkedList<Node>();
	private Map<Entity, Node> entityMap = new HashMap<Entity, EntityDependencyGraph.Node>();

	public EntityDependencyGraph(List<Entity> entities) {
		this.entityList = entities;

		// create nodes
		for (Entity e : this.entityList) {
			Node node = new Node(e);

			nodes.add(node);
			entityMap.put(e, node);
		}

		buildSerialDependencyGraph();
		
		
		Log.debug("Arranged serial schedule for entity construction:");
		Log.debug("Serial schedule: " + Glue.concat(nodes, " --> "));
	}
	/**
	 * Builds a serial schedule of building of entities into existing node list.
	 * Worst time complexity is <code>O(N!)</code> but the average time complexity
	 * converges to <code>O(N^2)</code>.
	 * 
	 * Postcondition: <code>EntityDependencyGraph.nodes</code> is ordered list 
	 * of serializable schedule.
	 */
	//TODO test more cases.
	private void buildSerialDependencyGraph() {
		for (Entity e : this.entityList) { // for each entity
			for (Field f : e.getFields()) {
				// detect one to one & one to many connections
				if (!f.isList() && getEntityOfType(f.getClazz()) != null) {
					Entity requirement = getEntityOfType(f.getClazz());
					entityMap.get(e).requires.add(requirement);
				}
			}
		}

		// sort nodes by number of requirement vertices in ascending order
		// it will improve algorithm running time on the average.
		Collections.sort(nodes);
		
		
		// algorithm:
		// 1. 	choose first unchecked node N from node list. 
		// 2. 	if all dependencies of N appear before N
		// 2T		mark N as checked
		//    	else
		// 2F1 		if N is at the end of the list
		// 2F1T			throw exception (cyclic graph detected)
		// 			else
		// 2F1F 		move N to one right position in the list.
		// 2F2		goto 2.
		LinkedList<Node> orderedList = new LinkedList<Node>();
		
		for(Node n : nodes)
			orderedList.add(n);
		
		while (true){
			Node unchecked = null;
			int pos = 0;
			for(Node n : orderedList){
				if (!n.checked){
					unchecked = n;
					break;
				}
				pos++;
			}
			
			if (unchecked == null) break;
			
			
			boolean dependenciesSatisfied = true;
			do { // iterate to right until match found.
				dependenciesSatisfied = true;
				for(Entity dependency : unchecked.requires){
					boolean satisfied = false;
					for(int i = 0; i < pos; i++){
						if (orderedList.get(i).entity == dependency){
							satisfied = true;
							break;
						}
					}
					dependenciesSatisfied &= satisfied;
				}
				
				if(dependenciesSatisfied){
					// save position
					unchecked.checked = true;
					break;
				} else {
					// move to right.
					if (pos >= orderedList.size()-1){
						// already at the end
						throw new UnsolvableDependencyOrderException();
					} else {
						orderedList.remove(pos);
						orderedList.add(pos+1, unchecked);
						pos++;
					}
				}
			} while (!dependenciesSatisfied);
		}
		
		nodes = orderedList;
	}

	/**
	 * @return {@link Entity} instance of given type from existing entity list,
	 *         or <code>null</code> if not found.
	 * 
	 * @param c
	 *            required class type
	 */
	private Entity getEntityOfType(Class<?> c) {
		for (Entity e : this.entityList)
			if (e.getType().equals(c)) {
				return e;
			}
		return null;
	}

	/**
	 * @return list of entities to construct them in returned order to obey
	 *         dependency.
	 */
	public List<Entity> getConstructSchedule(){
		// clone already arranged list
		List<Entity> schedule = new ArrayList<Entity>(nodes.size());
		for (Node n : nodes){
			schedule.add(n.entity);
		}
		return schedule;
	}
	
	/**
	 * @return list of entities to destruct them in returned order to obey
	 *         dependency (exactly reversed list of construct schedule).
	 * @see EntityDependencyGraph#getConstructSchedule() 
	 */
	public List<Entity> getDestroySchedule(){
		// clone already arranged list in reverse order
		List<Entity> schedule = new ArrayList<Entity>(nodes.size());
		
		// caution: O(N^2) time complexity but saves memory space
		// the reason we don't use LinkedList.descendingIterator is,
		// it is not implemented Android API<9.
		for(int i = nodes.size()-1, j = 0; i >= 0; i--, j++){
			schedule.add(j, nodes.get(i).entity);
		}
		return schedule;
	}

}
