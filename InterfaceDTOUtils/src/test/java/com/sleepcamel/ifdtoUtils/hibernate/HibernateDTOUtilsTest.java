package com.sleepcamel.ifdtoUtils.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.Test;

public class HibernateDTOUtilsTest {

	private static EntityManager entityManager;
	private static Session session;

	@Test
	public void testSimplifiedUser(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user order by user.id";
		
		Query query = session.createQuery(hql);
		
		List<ISimplifiedUser> resultList = HibernateDTOUtils.getFor(ISimplifiedUser.class).fromQuery(query);
		
		for(int i=0; i < resultList.size(); i++){
			assertDTO(i, resultList.get(i));
		}
	}
	
	@Test
	public void testSortSimplifiedUser(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user order by user.id";
		Query query = session.createQuery(hql);
		
		List<ISimplifiedUser> resultList = HibernateDTOUtils.getFor(ISimplifiedUser.class).fromQuery(query);
		Collections.sort(resultList, new SimplifiedUserComparator());
		
		List<ISimplifiedUser> sortedResultList = HibernateDTOUtils.getFor(ISimplifiedUser.class)
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
	public void testEmptyList(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user where user.name = :noName";
		Query query = session.createQuery(hql);
		query.setParameter("noName", "somethingThatIsNot");
		
		List<ISimplifiedUser> resultList = HibernateDTOUtils.getFor(ISimplifiedUser.class).fromQuery(query);
		assertTrue(resultList.isEmpty());
	}
	
	
	private void assertDTO(int i, ISimplifiedUser simplifiedUser){
		assertEquals(simplifiedUser.getName(),"User name "+i%5);
		assertEquals(simplifiedUser.getAvatar().getUrl(),"url of avatar "+i%10);
		assertEquals(simplifiedUser.getLocation().getName(),"Location "+i%20);
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
		
		entityManager.persist(user);
	}
}

interface ISimplifiedUser{
	
	String getName();
	
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
