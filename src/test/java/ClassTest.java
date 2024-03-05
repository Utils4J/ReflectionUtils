import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.exception.ClassNotFoundException;
import de.cyklon.reflection.exception.ConstructorNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClassTest {
	public static class StaticTestClass {
		private final String y;

		public StaticTestClass(String y) {
			this.y = y;
		}
	}

	public class TestClass {
		private final String y;

		public TestClass(String y) {
			this.y = y;
		}
	}

	@Test
	public void subclass() {
		ReflectClass<?> clazz = ReflectClass.wrap(ClassTest.class);

		assertEquals("TestClass", clazz.getSubclass("ClassTest$TestClass").getSimpleName());
		assertEquals("StaticTestClass", clazz.getSubclass("ClassTest$StaticTestClass").getSimpleName());

		assertThrows(ClassNotFoundException.class, () -> clazz.getSubclass("Test"));
	}

	@Test
	public void instance() {
		ReflectClass<?> clazz = ReflectClass.wrap(ClassTest.class);

		clazz.newInstance();
		assertThrows(ConstructorNotFoundException.class, () -> clazz.newInstance(""));
	}

	@Test
	public void subclassInstance() {
		ReflectClass<?> clazz = ReflectClass.wrap(ClassTest.class).getSubclass("ClassTest$TestClass");

		Object test = clazz.newInstance(this, "test");
		ReflectClass<?> type = ReflectClass.wrap(test.getClass());

		assertEquals("ClassTest", type.getNestParent().getName());
		assertEquals("test", type.getField("y").getValue(test));
	}

	@Test
	public void staticSubclassTest() {
		ReflectClass<?> clazz = ReflectClass.wrap(ClassTest.class).getSubclass("ClassTest$StaticTestClass");

		Object test = clazz.newInstance("test");
		ReflectClass<?> type = ReflectClass.wrap(test.getClass());

		assertEquals("ClassTest", type.getNestParent().getName());
		assertEquals("test", type.getField("y").getValue(test));
	}
}
