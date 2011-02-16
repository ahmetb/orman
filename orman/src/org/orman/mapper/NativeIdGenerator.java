package org.orman.mapper;

import java.util.Random;

import org.orman.mapper.annotation.Id;
import org.orman.mapper.exception.UnsupportedIdFieldTypeException;

/**
 * Implementation for {@link IdGenerationPolicy}.ORMAN_ID_GENERATION type.
 * 
 * Experimental {@link Id} generator for String, int and long fields. Uses time
 * seed, randomizer and Model instance to produce a Id key.
 * 
 * Warning: DOES NOT GUARANTEE preventing collisions, however collisions are
 * very unlikely.
 * 
 * @author alp
 * 
 */

public class NativeIdGenerator {

	private static Random r = new Random(System.nanoTime());

	/**
	 * 
	 * @return {@link Object} instance, however underlying object will be type
	 *         of {@link Integer}, {@link String} or {@link Long} according to
	 *         {@link Id} field of given {@link Entity}.
	 */
	public static Object generate(Field field, Model<?> instance) {
		Class<?> idType = field.getClazz();

		if (instance == null)
			throw new IllegalArgumentException(
					"Instance parameter can not be null to generate Id.");

		if (idType.equals(String.class)) {
			return generateString(instance);
		} else if (idType.equals(Long.class) || idType.equals(Long.TYPE)) {
			return generateLong(instance);
		} else if (idType.equals(Integer.class) || idType.equals(Integer.TYPE)) {
			return generateInteger(instance);
		}

		/*
		 * if not matches occur, then an unsupported tye has been sent to the
		 * method, which is very unlikely if the access violations have not
		 * occurred.
		 */
		throw new UnsupportedIdFieldTypeException(idType.getName());
	}

	private static String generateString(Model<?> instance) {
		return Long.toHexString((instance.hashCode()^randLongSeed())) + ""
				+ Long.toHexString(instance.hashCode() ^ randSeed() | randLongSeed() & timeSeed());
	}

	private static Long generateLong(Model<?> instance) {
		return Math.abs(Math.abs(timeSeed() ^ instance.hashCode()
				* (randSeed() ^ timeSeed() ^ instance.hashCode())) % Long.MAX_VALUE
				| instance.hashCode()
				^ timeSeed()
				+ instance.hashCode()
				^ randLongSeed());
	}

	private static Integer generateInteger(Model<?> instance) {
		return Math.abs(instance.hashCode() | randSeed() + instance.hashCode()
				^ ((int) (timeSeed() >> 32) | Integer.MAX_VALUE));
	}

	private static long timeSeed() {
		return System.nanoTime();
	}

	private static int randSeed() {
		return Math.abs(r.nextInt());
	}

	private static long randLongSeed() {
		return Math.abs(r.nextLong());
	}
}
