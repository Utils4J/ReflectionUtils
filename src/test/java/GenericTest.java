import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectField;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GenericTest {
	public final List<String>[][][] array = null;
	public final Map<String, List<Integer>> map = null;

	public final List<? extends String> wildcard = null;

	@Test
	public void array() {
		ReflectField<?, ?> field = ReflectClass.wrap(GenericTest.class).getField("array");
		ReflectClass<?> type = field.getReturnType();
		ReflectClass.ArrayInfo array = type.getArrayInfo();

		assertEquals("java.util.List<java.lang.String>", array.component().toString());
		assertEquals(3, array.depth());
	}

	@Test
	public void component() {
		ReflectField<?, ?> field = ReflectClass.wrap(GenericTest.class).getField("map");
		ReflectClass<?> type = field.getReturnType();
		ReflectClass<?> key = type.getTypeParameters().get(0);
		ReflectClass<?> value = type.getTypeParameters().get(1);

		assertEquals("java.lang.String", key.toString());
		assertEquals("java.util.List<java.lang.Integer>", value.toString());
	}

	@Test
	public void wildcard() {
		ReflectField<?, ?> field = ReflectClass.wrap(GenericTest.class).getField("wildcard");
		ReflectClass<?> type = field.getReturnType();
		ReflectClass<?> component = type.getTypeParameters().get(0);

		assertTrue(component.isWildcard());
		assertEquals("? extends java.lang.String", component.toString());
		assertEquals("java.lang.String", component.getUpperWildcardBounds().get(0).toString());
	}
}
