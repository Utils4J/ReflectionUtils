import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectField;
import de.cyklon.reflection.exception.FieldNotFoundException;
import de.cyklon.reflection.types.Modifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
	public static String test = "";
	public String name = "abc";

	@Test
	public void get() {
		ReflectField<?, ?> field = ReflectClass.wrap(FieldTest.class).getField("name");
		assertEquals("name", field.getName());
		assertEquals("java.lang.String", field.getReturnType().toString());

		assertThrows(FieldNotFoundException.class, () -> ReflectClass.wrap(GenericTest.class).getField("test"));
	}

	@Test
	public void value() {
		ReflectField<FieldTest, String> field = ReflectClass.wrap(FieldTest.class).getField(String.class, "name");
		assertEquals("abc", field.getValue(this));

		field.setValue(this, "def");
		assertEquals("def", field.getValue(this));
	}

	@Test
	public void staticValue() {
		ReflectField<FieldTest, String> field = ReflectClass.wrap(FieldTest.class).getField(String.class, "test");
		assertEquals("", field.getStaticValue());

		field.setStaticValue("hello");
		assertEquals("hello", field.getStaticValue());
	}

	@Test
	public void modifiers() {
		ReflectField<FieldTest, String> field = ReflectClass.wrap(FieldTest.class).getField(String.class, "name");
		assertFalse(field.getModifiers().contains(Modifier.STATIC));

		field = ReflectClass.wrap(FieldTest.class).getField(String.class, "test");
		assertTrue(field.getModifiers().contains(Modifier.STATIC));
	}
}
