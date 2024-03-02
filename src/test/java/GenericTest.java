import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectField;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GenericTest {
	public final List<String>[][][] test = null;

	@Test
	public void array() {
		ReflectField<?, ?> field = ReflectClass.wrap(GenericTest.class).getField("test");
		ReflectClass<?> type = field.getReturnType();
		ReflectClass.ArrayInfo array = type.getArrayInfo();

		assertEquals("java.util.List<java.lang.String>", array.component().toString());
		assertEquals(3, array.depth());
	}
}
