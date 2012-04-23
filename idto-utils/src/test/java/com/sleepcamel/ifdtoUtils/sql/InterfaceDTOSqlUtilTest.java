package com.sleepcamel.ifdtoUtils.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InterfaceDTOSqlUtilTest {

	private static Connection connection;
	private static java.util.Date macDoB;
	private static java.util.Date jennyDoB;

	@Test
	@SuppressWarnings("deprecation")
	public void testSingleResult() throws SQLException {
		String queryString = "SELECT * from user where name = 'jenny'";
		Statement statement = connection.createStatement();
		
		ISqlUser sqlUser = InterfaceDTOSqlUtils.getFor(ISqlUser.class).fromQuery(queryString).withStatement(statement).result();
		Long age = 22L;
		
		assertNotNull(sqlUser);
		assertEquals(sqlUser.getName(),"jenny");
		assertEquals(sqlUser.getPass(),"1213456");
		assertEquals(sqlUser.isAdmin(),true);
		assertEquals(sqlUser.getAge(),age);
		assertEquals(sqlUser.getDoB().getYear(),jennyDoB.getYear());
		assertEquals(sqlUser.getDoB().getMonth(),jennyDoB.getMonth());
		assertEquals(sqlUser.getDoB().getDay(),jennyDoB.getDay());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void testWithConnection() throws SQLException {
		String queryString = "SELECT * from user where name = 'mac'";
		
		ISqlUser sqlUser = InterfaceDTOSqlUtils.getFor(ISqlUser.class).fromQuery(queryString).withConnection(connection).result();
		Long age = 25L;
		
		assertNotNull(sqlUser);
		assertEquals(sqlUser.getName(),"mac");
		assertEquals(sqlUser.getPass(),"22");
		assertEquals(sqlUser.isAdmin(),false);
		assertEquals(sqlUser.getAge(),age);
		assertEquals(sqlUser.getDoB().getYear(),macDoB.getYear());
		assertEquals(sqlUser.getDoB().getMonth(),macDoB.getMonth());
		assertEquals(sqlUser.getDoB().getDay(),macDoB.getDay());
	}

	@Test
	public void testUniqueNoResult() throws SQLException {
		String queryString = "SELECT * from user where id = 1000";
		assertNull(InterfaceDTOSqlUtils.getFor(ISqlUser.class).fromQuery(queryString).withConnection(connection).result());
	}
	
	@Test
	public void testListNoResults() throws SQLException {
		String queryString = "SELECT * from user where name like 'thing'";
		List<ISqlUser> sqlUsers = InterfaceDTOSqlUtils.getFor(ISqlUser.class).fromQuery(queryString).withConnection(connection).list().result();
		assertNotNull(sqlUsers);
		assertTrue(sqlUsers.isEmpty());
	}

	@Test
	public void testList() throws SQLException {
		String queryString = "SELECT * from user order by id asc";
		
		List<ISqlUser> sqlUsers = InterfaceDTOSqlUtils.getFor(ISqlUser.class).fromQuery(queryString).withConnection(connection).list().result();
		
		assertNotNull(sqlUsers);
		assertEquals(sqlUsers.size(),3);
		
		assertEquals(sqlUsers.get(0).getName(),"jenny");
		assertEquals(sqlUsers.get(1).getName(),"mac");
		assertEquals(sqlUsers.get(2).getName(),"bk");
		assertNull(sqlUsers.get(2).getDoB());
	}
	
	@Test
	public void testSortList() throws SQLException {
		String queryString = "SELECT * from user order by id asc";
		
		InterfaceDTOSqlListUtils<ISqlUser> utils = InterfaceDTOSqlUtils.getFor(ISqlUser.class).fromQuery(queryString).withConnection(connection).list();

		List<ISqlUser> sqlUsers = utils.sort(new Comparator<ISqlUser>() {													
			public int compare(ISqlUser arg0, ISqlUser arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
		}).result();
		
		assertNotNull(sqlUsers);
		assertEquals(sqlUsers.size(),3);
		
		assertEquals(sqlUsers.get(0).getName(),"bk");
		assertNull(sqlUsers.get(0).getDoB());
		assertEquals(sqlUsers.get(1).getName(),"jenny");
		assertEquals(sqlUsers.get(2).getName(),"mac");
	}
	
	@Test
	public void testConverter() throws SQLException {
		String queryString = "SELECT u.name, p.name, p.type from user u inner join user_pet up on u.id = up.user_id "+
				 "inner join pet p on p.id = up.pet_id";

		List<IUserWithPet> usersWithPets = InterfaceDTOSqlUtils.getFor(IUserWithPet.class)
																.fromQuery(queryString)
																.converter(PetType.class, PetTypeConverter.INSTANCE)
																.withConnection(connection)
																.list().result();
		
		assertNotNull(usersWithPets);
		assertEquals(usersWithPets.size(),8);
		
		assertEquals(usersWithPets.get(0).getPetType(),PetType.DOG);
	}

	@Test
	public void testJoin() throws SQLException {
		String queryString = "SELECT u.name, p.name, p.type from user u inner join user_pet up on u.id = up.user_id "+
							 "inner join pet p on p.id = up.pet_id";
		
		List<IUserWithPet> usersWithPets = InterfaceDTOSqlUtils.getFor(IUserWithPet.class)
																.fromQuery(queryString)
																.withConnection(connection)
																.list().result();
		
		assertNotNull(usersWithPets);
		assertEquals(usersWithPets.size(),8);
		
		assertEquals(usersWithPets.get(0).getUserName(),"jenny");
		assertEquals(usersWithPets.get(0).getPetName(),"Indiana");
		assertEquals(usersWithPets.get(0).getPetType(),PetType.DOG);
		
		assertEquals(usersWithPets.get(4).getUserName(),"mac");
		assertEquals(usersWithPets.get(4).getPetName(),"Indiana");
		assertEquals(usersWithPets.get(4).getPetType(),PetType.DOG);
		
		assertEquals(usersWithPets.get(7).getUserName(),"mac");
		assertEquals(usersWithPets.get(7).getPetName(),"Oliver");
		assertEquals(usersWithPets.get(7).getPetType(),PetType.CAT);
	}

	@Test
	public void testCount() throws SQLException {
		String queryString = "SELECT u.name, count(up.pet_id) from user u "+
				 			 " inner join user_pet up on u.id = up.user_id "+
				 			 " inner join pet p on p.id = up.pet_id "+
				 			 " where p.type = 'DOG' group by u.name";

		List<IUserPetCount> userPetsCount = InterfaceDTOSqlUtils.getFor(IUserPetCount.class)
															.fromQuery(queryString)
															.withConnection(connection)
															.list().result();
		
		assertNotNull(userPetsCount);
		assertEquals(userPetsCount.size(), 2);
		
		assertEquals(userPetsCount.get(0).getUserName(), "jenny");
		assertEquals(userPetsCount.get(0).getCount(), 4);
		
		assertEquals(userPetsCount.get(1).getUserName(), "mac");
		assertEquals(userPetsCount.get(1).getCount(), 2);
	}
	
	@Test
	public void testMap() throws SQLException {
		String queryString = "SELECT u.name, p.type, count(p.id) from user u "+
				 			 " inner join user_pet up on u.id = up.user_id "+
				 			 " inner join pet p on p.id = up.pet_id "+
				 			 " group by u.name, p.type";

		Map<String, Map<PetType, IUserPetTypeCount>> userPetTypeCounts = InterfaceDTOSqlUtils.getFor(IUserPetTypeCount.class)
															.fromQuery(queryString)
															.withConnection(connection)
															.map(PetType.class, 1).key(String.class, 0).result();
		
		assertNotNull(userPetTypeCounts);
		assertEquals(userPetTypeCounts.size(), 2);
		
		assertTrue(userPetTypeCounts.containsKey("jenny"));
		assertTrue(userPetTypeCounts.get("jenny").containsKey(PetType.DOG));
		assertFalse(userPetTypeCounts.get("jenny").containsKey(PetType.CAT));
		assertEquals(userPetTypeCounts.get("jenny").get(PetType.DOG).getCount(), 4);
		
		assertTrue(userPetTypeCounts.containsKey("mac"));
		assertTrue(userPetTypeCounts.get("mac").containsKey(PetType.DOG));
		assertTrue(userPetTypeCounts.get("mac").containsKey(PetType.CAT));
		assertEquals(userPetTypeCounts.get("mac").get(PetType.DOG).getCount(), 2);
		assertEquals(userPetTypeCounts.get("mac").get(PetType.CAT).getCount(), 2);
	}

	@Before
	public void createDB() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver").newInstance();
		String connectionURL = "jdbc:h2:mem:testdb";

		connection = DriverManager.getConnection(connectionURL, "sa", "");

		Statement statement = connection.createStatement();

		// Create tables
		String queryString = "CREATE TABLE user (id INTEGER NOT NULL AUTO_INCREMENT, "
				+ "name VARCHAR(25), pass VARCHAR(15), "
				+ "is_admin BOOLEAN, age BIGINT, "
				+ "dob TIMESTAMP, primary key(id))";
		statement.executeUpdate(queryString);
		
		queryString = "CREATE TABLE pet (id INTEGER NOT NULL AUTO_INCREMENT, "
				+ "name VARCHAR(25), type VARCHAR(15), primary key(id))";
		statement.executeUpdate(queryString);
		
		queryString = "CREATE TABLE user_pet (user_id INTEGER NOT NULL , "
				+ "pet_id INTEGER, primary key(user_id, pet_id), "
				+ "foreign key(user_id) references user(id), "
				+ "foreign key(pet_id) references pet(id) )";
		statement.executeUpdate(queryString);

		// Insert Users
		jennyDoB = new java.util.Date(System.currentTimeMillis());
		queryString = "INSERT INTO user (name, pass, is_admin, age, dob) VALUES " +
					  "('jenny', '1213456', true, 22, '" + new Date(jennyDoB.getTime()) + "')";
		statement.executeUpdate(queryString);

		macDoB = new java.util.Date(System.currentTimeMillis());
		queryString = "INSERT INTO user (name, pass, is_admin, age, dob) VALUES " +
					  "('mac', '22', false, 25, '" + new Date(macDoB.getTime())  + "')";
		statement.executeUpdate(queryString);

		queryString = "INSERT INTO user (name, pass, is_admin, age) VALUES " +
					  "('bk', '22', false, 25)";
		statement.executeUpdate(queryString);

		// Insert Pets
		queryString = "INSERT INTO pet (name, type) VALUES ('Indiana', 'DOG')";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO pet (name, type) VALUES ('Max', 'DOG')";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO pet (name, type) VALUES ('Bella', 'DOG')";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO pet (name, type) VALUES ('Charlie', 'DOG')";
		statement.executeUpdate(queryString);
		
		queryString = "INSERT INTO pet (name, type) VALUES ('Tigger', 'CAT')";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO pet (name, type) VALUES ('Lucy', 'CAT')";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO pet (name, type) VALUES ('Oliver', 'CAT')";
		statement.executeUpdate(queryString);

		queryString = "INSERT INTO user_pet VALUES (1, 1)";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO user_pet VALUES (1, 2)";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO user_pet VALUES (1, 3)";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO user_pet VALUES (1, 4)";
		statement.executeUpdate(queryString);
		
		queryString = "INSERT INTO user_pet VALUES (2, 1)";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO user_pet VALUES (2, 3)";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO user_pet VALUES (2, 5)";
		statement.executeUpdate(queryString);
		queryString = "INSERT INTO user_pet VALUES (2, 7)";
		statement.executeUpdate(queryString);
		
		statement.close();
	}

	@After
	public void closeConnection() throws SQLException {
		connection.close();
	}
}

interface ISqlUser{
	Integer getId();
	String getName();
	String getPass();
	Boolean isAdmin();
	Long getAge();
	java.util.Date getDoB();
}

interface IUserWithPet{
	String getUserName();
	String getPetName();
	PetType getPetType();
}

interface IUserPetCount{
	String getUserName();
	long getCount();
}

interface IUserPetTypeCount{
	String getUserName();
	PetType getPetType();
	long getCount();
}