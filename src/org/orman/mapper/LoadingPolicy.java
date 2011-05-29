package org.orman.mapper;

/**
 * Describes loading policy between entitiy relations.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 *
 */
public enum LoadingPolicy {

	/**
	 * Loads target entity at loading of parent 
	 * entity. May cause huge overheads. 
	 */
	EAGER,
	
	/**
	 * Does not load target entity at loading of
	 * parent entity, leaves it as <code>null</code>
	 * and defers its query to the developer. 
	 * 
	 * It can be coded using {@link ModelQuery}
	 * builder. Should be the preferred case
	 * on most cardinalities where one side of the
	 * statement is "many".
	 */
	LAZY,
}
