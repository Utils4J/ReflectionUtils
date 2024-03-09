import de.cyklon.reflection.entities.OfflinePackage;
import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectPackage;
import de.cyklon.reflection.exception.ClassNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import test.TestPackage;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PackageTest {

	private static final ReflectPackage pkg = ReflectClass.wrap(PackageTest.class).getPackage();
	private static final OfflinePackage testPkg = pkg.getPackage("test");

	@Test
	public void base() {
		assertTrue(pkg.isBasePackage());
		assertFalse(testPkg.isBasePackage());
	}

	@Test
	public void parent() {
		assertNull(pkg.getParent());
		assertNotNull(testPkg.getParent());
	}

	@Test
	public void clazz() {
		pkg.getLoadedClass(getClass().getName());
		assertThrows(ClassNotFoundException.class, () -> pkg.getLoadedClass("abc"));
	}

	@Test
	@Order(0)
	public void load() {
		assertNotNull(testPkg, "test package not found");
		assertFalse(testPkg.isLoaded());

		testPkg.load();

		assertTrue(testPkg.isLoaded(), "timeout during loading");
	}

	@Test
	@Order(1)
	public void annotation() {
		ReflectPackage tp = testPkg.load();
		assertNotNull(tp.getAnnotation(TestPackage.class), "annotation not found");
	}

	@Test
	@Order(1)
	public void child() {
		ReflectPackage tp = testPkg.load();

		OfflinePackage p = tp.getPackage("t1");

		assertNotNull(p, "child package not found");
		assertEquals("test.t1", p.getName());
		assertEquals("t1", p.getSimpleName());

		assertEquals(2, tp.getDirectClasses().size());
		assertEquals(3, tp.getClasses().size());
		assertEquals(1, p.getClasses().size());
	}
}
