import de.cyklon.reflection.entities.OfflinePackage;
import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectPackage;
import de.cyklon.reflection.function.Filter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import test.TestPackage;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PackageTest {

	private static ReflectPackage pkg = ReflectClass.wrap(PackageTest.class).getPackage();
	private static OfflinePackage testPkg = pkg.getPackages(Filter.hasSimpleName("test")).stream().findFirst().orElse(null);

	@Test
	public void base() {
		assertTrue(pkg.isBasePackage());
	}

	@Test
	public void parent() {
		assertNull(pkg.getParent());
	}

	@Test
	public void classes() {
		assertDoesNotThrow(() -> pkg.getClass(getClass().getName()));
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
	@Order(2)
	public void child() {
		ReflectPackage tp = testPkg.load();

		OfflinePackage p = tp.getPackages(Filter.hasSimpleName("t1")).stream().findFirst().orElse(null);

		assertNotNull(p, "child package not found");
		assertEquals("test.t1", p.getName());
		assertEquals("t1", p.getSimpleName());
	}
}
