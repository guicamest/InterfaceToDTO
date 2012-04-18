package com.sleepcamel.ifdtoUtils.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sleepcamel.ifdtoUtils.exception.DTOUtilsException;

public class HibernateDTOUtilsTest {

	private static final int USER_ID_WITH_LAST_NAME = 15;
	private static final String USER_HAS_LAST_NAME = "User has last name";
	private static EntityManager entityManager;
	private static Session session;

	@Test
	public void testSingleResult(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user where user.lastName = :lastName";
		Query query = session.createQuery(hql);
		query.setParameter("lastName", USER_HAS_LAST_NAME);
		
		ISimplifiedUser user = HibernateDTOUtils.getFor(ISimplifiedUser.class).fromQuery(query);
		assertNotNull(user);
		assertDTO(USER_ID_WITH_LAST_NAME, user);
		
		List<ISimplifiedUser> resultList = HibernateDTOUtils.getFor(ISimplifiedUser.class).list().fromQuery(query);
		assertEquals(resultList.size(),1);
		assertDTO(USER_ID_WITH_LAST_NAME, resultList.get(0));
		
		Map<Avatar, ISimplifiedUser> resultMap = HibernateDTOUtils.getFor(ISimplifiedUser.class)
																  .map(Avatar.class,1)
																  .fromQuery(query);
		assertEquals(resultMap.size(),1);
		
		Set<Avatar> keySet = resultMap.keySet();
		assertEquals(keySet.size(),1);
		
		Collection<ISimplifiedUser> mapValues = resultMap.values();
		assertEquals(mapValues.size(),1);
		
		ISimplifiedUser next = mapValues.iterator().next();
		assertEquals(keySet.iterator().next(),next.getAvatar());
		
		assertDTO(USER_ID_WITH_LAST_NAME, next);
	}

	@Test
	public void testSetter(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user where user.lastName = :lastName";
		Query query = session.createQuery(hql);
		query.setParameter("lastName", USER_HAS_LAST_NAME);
		
		ISimplifiedUser user = HibernateDTOUtils.getFor(ISimplifiedUser.class).fromQuery(query);
		assertEquals(user.getLastName(), USER_HAS_LAST_NAME);
		user.setLastName("New user Last Name");
		assertEquals(user.getLastName(), "New user Last Name");
	}

	@Test
	public void testNoResult(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user where user.name = :noName";
		Query query = session.createQuery(hql);
		query.setParameter("noName", "somethingThatIsNot");
		
		List<ISimplifiedUser> resultList = HibernateDTOUtils.getFor(ISimplifiedUser.class).list().fromQuery(query);
		assertTrue(resultList.isEmpty());
		
		Map<Location, ISimplifiedUser> resultMap = HibernateDTOUtils.getFor(ISimplifiedUser.class).map(Location.class,0).fromQuery(query);
		assertTrue(resultMap.isEmpty());
		
		ISimplifiedUser object = HibernateDTOUtils.getFor(ISimplifiedUser.class).fromQuery(query);
		assertNull(object);
	}
	
	@Test
	public void testSimplifiedUser(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user order by user.id";
		
		Query query = session.createQuery(hql);
		
		List<ISimplifiedUser> resultList = HibernateDTOUtils.getFor(ISimplifiedUser.class).list().fromQuery(query);
		
		for(int i=0; i < resultList.size(); i++){
			assertDTO(i, resultList.get(i));
		}
	}
	
	@Test
	public void testSortSimplifiedUser(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user order by user.id";
		Query query = session.createQuery(hql);
		
		List<ISimplifiedUser> resultList = HibernateDTOUtils.getFor(ISimplifiedUser.class).list().fromQuery(query);
		Collections.sort(resultList, new SimplifiedUserComparator());
		
		List<ISimplifiedUser> sortedResultList = HibernateDTOUtils.getFor(ISimplifiedUser.class)
															.list()
															.sort(new SimplifiedUserComparator())
															.fromQuery(query);
		
		assertNotSame(resultList, sortedResultList);
		assertEquals(resultList.size(), sortedResultList.size());
		for(int i=0; i < resultList.size(); i++){
			ISimplifiedUser resultListUser = resultList.get(i);
			ISimplifiedUser sortedListUser = sortedResultList.get(i);
			assertEquals(resultListUser.getName(), sortedListUser.getName());
			assertEquals(resultListUser.getAvatar().getUrl(), sortedListUser.getAvatar().getUrl());
			assertEquals(resultListUser.getLocation().getName(), sortedListUser.getLocation().getName());
			assertEquals(resultListUser.getLastName(), sortedListUser.getLastName());
		}
	}
	
	@Test
	public void testSimpleMap(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user";
		Query query = session.createQuery(hql);
		Map<Avatar, ISimplifiedUser> resultMap = HibernateDTOUtils.getFor(ISimplifiedUser.class).map(Avatar.class,1)
														.fromQuery(query);
		assertEquals(resultMap.size(), 30);
		for(Entry<Avatar, ISimplifiedUser> entry:resultMap.entrySet()){
			assertEquals(entry.getKey(), entry.getValue().getAvatar());
		}
	}
	
	@Test
	public void testMapMultipleKeys(){
		String hql = "select user.name, user.avatar, user.location from User user";
		Query query = session.createQuery(hql);
		Map<String, Map<Avatar, ISimplifiedUser>> resultMap = HibernateDTOUtils.getFor(ISimplifiedUser.class)
														.map(Avatar.class,1)
														.key(String.class,0)
														.fromQuery(query);
		assertEquals(resultMap.size(), 5);
		for(Entry<String, Map<Avatar, ISimplifiedUser>> entry:resultMap.entrySet()){
			assertEquals(entry.getValue().size(), 6);
			for(Entry<Avatar, ISimplifiedUser> subEntry:entry.getValue().entrySet()){
				assertEquals(subEntry.getKey(), subEntry.getValue().getAvatar());
				assertEquals(entry.getKey(), subEntry.getValue().getName());
			}
		}
	}
	
	@Test
	public void testMapList(){
		String hql = "select user.name, user.avatar, user.location from User user";
		Query query = session.createQuery(hql);
		Map<Location, List<ISimplifiedUser>> resultMap = HibernateDTOUtils.getFor(ISimplifiedUser.class)
														.mapList(Location.class,2)
														.fromQuery(query);
		assertEquals(resultMap.size(), 20);
		for(Entry<Location, List<ISimplifiedUser>> entry:resultMap.entrySet()){
			Location key = entry.getKey();
			if ( (key.id-1) % 20 < 10 ){
				assertEquals(entry.getValue().size(), 2);
				assertEquals(entry.getValue().get(0).getLocation(), key);
				assertEquals(entry.getValue().get(1).getLocation(), key);
			}else{
				assertEquals(entry.getValue().size(), 1);
				assertEquals(entry.getValue().get(0).getLocation(), key);
			}
		}
	}
	
	@Test(expected=DTOUtilsException.class)
	public void testInvalidMapIndex(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user";
		Query query = session.createQuery(hql);
		HibernateDTOUtils.getFor(ISimplifiedUser.class).map(Avatar.class,4)
														.fromQuery(query);
	}
	
	@SuppressWarnings("deprecation")
	@Test(expected=DTOUtilsException.class)
	public void testDontDoubleMap(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user";
		Query query = session.createQuery(hql);
		HibernateDTOUtils.getFor(ISimplifiedUser.class).map(Avatar.class,4).map(Avatar.class, 4)
														.fromQuery(query);
	}
	
	@Test
	public void testBuilderInstancesAreSame(){
		HibernateDTOListUtils<ISimplifiedUser> listBuilder = HibernateDTOUtils.getFor(ISimplifiedUser.class).list();
		assertEquals(listBuilder, listBuilder.list());
	}
	
	private void assertDTO(int i, ISimplifiedUser simplifiedUser){
		assertEquals(simplifiedUser.getName(),"User name "+i%5);
		assertEquals(simplifiedUser.getAvatar().getUrl(),"url of avatar "+i%10);
		assertEquals(simplifiedUser.getLocation().getName(),"Location "+i%20);
		if ( i == USER_ID_WITH_LAST_NAME ){
			assertEquals(simplifiedUser.getLastName(),USER_HAS_LAST_NAME);
		}else{
			assertNull(simplifiedUser.getLastName());
		}
	}

	public void dropDB() {
		entityManager.close();
	}

	@BeforeClass
	public static void createDB() {
		entityManager = Persistence.createEntityManagerFactory("dtoUtilsUnit").createEntityManager();
		entityManager.getTransaction().begin();
		
		for(int i=0; i < 30; i++){
			createUser(i);
		}
		
		entityManager.getTransaction().commit();
		session = ((Session)entityManager.getDelegate());
	}

	private static void createUser(int i) {
		User user = new User();
		user.setName("User name "+i%5);
		
		Avatar avatar = new Avatar();
		avatar.setUrl("url of avatar "+i%10);
		user.setAvatar(avatar);
		
		Location location = new Location();
		location.setName("Location "+i%20);
		user.setLocation(location);
		
		if ( i == 15 ){
			user.setLastName(USER_HAS_LAST_NAME);
		}
		
		entityManager.persist(user);
	}
}

interface ISimplifiedUser{
	
	String getName();
	
	void setLastName(String string);

	Avatar getAvatar();
	
	Location getLocation();
	
	String getLastName();
}

class SimplifiedUserComparator implements Comparator<ISimplifiedUser>{

	@Override
	public int compare(ISimplifiedUser o1, ISimplifiedUser o2) {
		return o1.getName().compareTo(o2.getName());
	}
	
}
