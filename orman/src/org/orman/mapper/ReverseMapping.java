package org.orman.mapper;

import org.orman.datasource.ResultList.ResultRow;

/**
 * Provides reverse mapping engine which can convert {@link ResultRow} objects
 * into instances whose their class definitions extend {@link Model}. (which
 * they are also {@link Entity}.
 * 
 * Has smart and safe type conversions so that returned data is appropriate.
 * 
 * @author alp
 */
public class ReverseMapping {
	@SuppressWarnings("unchecked")
	public static <E extends Model<?>> E map(ResultRow row, Class<E> type,
			Entity e) {

		E instance = null;

		try {
			instance = (E) e.getDefaultConstructor().newInstance();
		} catch (Exception e1) {
			// TODO assuming that no invocation exceptions will occur at
			// runtime.
			e1.printStackTrace(); // TODO log.
		}

		if (instance != null)
			for (Field f : e.getFields()) {
				// read field
				Object fieldValue = row.getColumn(f.getGeneratedName());

				// TODO do needed conversions.
				fieldValue = smartCasting(fieldValue, f.getClazz());
				
				// TODO make relational mapping if @*To* entities exist according to lazy loading policy.

				// set field
				instance.setEntityField(f, e, fieldValue);
			}

		return instance;
	}
	

	
	/**
	 * makes conversions String<->long<->integer<->boolean.
	 * 
	 * @param value
	 * @param clazz
	 *            desired class type.
	 * @return casted instance. it may be newly created. do not rely on
	 *         reference. it may return the same instance if no eligible
	 *         changes are found. null if <code>value</code> is null.
	 */
	private static Object smartCasting(Object value, Class<?> desired) {
		if (value == null) return null;
		
		if (Integer.class.equals(desired) || Integer.TYPE.equals(desired)) {
			// destination: Integer.

			if (value.getClass().equals(Integer.class)
					|| value.getClass().equals(Integer.TYPE))
				return value;
			else
				return new Integer(value.toString());
		}

		if (Long.class.equals(desired) || Long.TYPE.equals(desired)) {
			// destination: Long.
			if (value.getClass().equals(Long.class)
					|| value.getClass().equals(Long.TYPE))
				return value;
			else
				return new Long(value.toString());
		}

		if (Float.class.equals(desired) || Float.TYPE.equals(desired)) {
			// destination: Float.
			if (value.getClass().equals(Float.class)
					|| value.getClass().equals(Float.TYPE))
				return value;
			else
				return new Float(value.toString());
		}

		if (Double.class.equals(desired) || Double.TYPE.equals(desired)) {
			// destination: Double.
			if (value.getClass().equals(Double.class)
					|| value.getClass().equals(Double.TYPE))
				return value;
			else
				return new Double(value.toString());
		}

		if (Boolean.class.equals(desired) || Boolean.TYPE.equals(desired)) {
			// destination: Double.
			if (value.getClass().equals(Boolean.class)
					|| value.getClass().equals(Boolean.TYPE))
				return value;
			else
				return ((Integer) smartCasting(value, Integer.TYPE)) > 0;
		}

		if (String.class.equals(desired)) {
			// destination: String.
			if (value.getClass().equals(String.class))
				return value;
			else
				return ((Integer) smartCasting(value, Integer.TYPE)) > 0;
		}

		return value;
	}
}
