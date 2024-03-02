import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.exception.MemberNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MethodTest {
	public static String staticTest(int x) {
		return "static " + (x * 2);
	}

	public String test(int x) {
		return String.valueOf(x * 2);
	}

	@Test
	public void get() {
		ReflectMethod<?, ?> method = ReflectClass.wrap(MethodTest.class).getMethod("test", int.class);
		assertEquals("test", method.getName());
		assertEquals("java.lang.String", method.getReturnType().toString());
		assertEquals("int", method.getParameters().get(0).getReturnType().toString());

		assertThrows(MemberNotFoundException.class, () -> ReflectClass.wrap(GenericTest.class).getMethod("test"));
	}

	@Test
	public void invoke() {
		ReflectMethod<MethodTest, ?> method = ReflectClass.wrap(MethodTest.class).getMethod("test", int.class);
		assertEquals("4", method.invoke(this, 2));
		assertEquals("8", method.invoke(this, 4));
	}

	@Test
	public void staticInvoke() {
		ReflectMethod<MethodTest, ?> method = ReflectClass.wrap(MethodTest.class).getMethod("staticTest", int.class);
		assertEquals("static 4", method.invokeStatic(2));
		assertEquals("static 8", method.invokeStatic(4));
	}
}
