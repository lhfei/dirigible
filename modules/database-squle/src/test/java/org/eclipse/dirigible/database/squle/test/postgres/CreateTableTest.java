package org.eclipse.dirigible.database.squle.test.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.dirigible.database.squle.DataType;
import org.eclipse.dirigible.database.squle.Squle;
import org.eclipse.dirigible.database.squle.dialects.postgres.PostgresSquleDialect;
import org.junit.Test;

public class CreateTableTest {
	
	@Test
	public void createTableGeneric() {
		String sql = Squle.getNative(new PostgresSquleDialect())
			.create()
			.table("CUSTOMERS")
			.column("ID", DataType.INTEGER, true, false, false)
			.column("FIRST_NAME", DataType.VARCHAR, false, false, true, "(20)")
			.column("LAST_NAME", DataType.VARCHAR, false, true, false, "(30)")
			.toString();
		
		assertNotNull(sql);
		assertEquals("CREATE TABLE CUSTOMERS ( ID INTEGER NOT NULL PRIMARY KEY , FIRST_NAME VARCHAR (20) NOT NULL UNIQUE , LAST_NAME VARCHAR (30) )", sql);
	}
	
	@Test
	public void createTableTypeSafe() {
		String sql = Squle.getNative(new PostgresSquleDialect())
			.create()
			.table("CUSTOMERS")
			.columnInteger("ID", true, false, false)
			.columnVarchar("FIRST_NAME", 20, false, true, true)
			.columnVarchar("LAST_NAME", 30, false, true, false)
			.toString();
		
		assertNotNull(sql);
		assertEquals("CREATE TABLE CUSTOMERS ( ID INTEGER NOT NULL PRIMARY KEY , FIRST_NAME VARCHAR (20) UNIQUE , LAST_NAME VARCHAR (30) )", sql);
	}

}