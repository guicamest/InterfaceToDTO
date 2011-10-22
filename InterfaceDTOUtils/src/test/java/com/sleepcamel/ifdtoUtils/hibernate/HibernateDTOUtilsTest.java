package com.sleepcamel.ifdtoUtils.hibernate;

import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Assert;
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
	public void testEmptyList(){
		String hql = "select user.name, user.avatar, user.location, user.lastName from User user where user.name = :noName";
		Query query = session.createQuery(hql);
		query.setParameter("noName", "somethingThatIsNot");
		
		List<ISimplifiedUser> resultList = HibernateDTOUtils.getFor(ISimplifiedUser.class).fromQuery(query);
		Assert.assertTrue(resultList.isEmpty());
	}
	
	
	private void assertDTO(int i, ISimplifiedUser simplifiedUser){
		Assert.assertEquals(simplifiedUser.getName(),"User name "+i%5);
		
		Assert.assertEquals(simplifiedUser.getAvatar().getUrl(),"url of avatar "+i%10);
		
		Assert.assertEquals(simplifiedUser.getLocation().getName(),"Location "+i%20);
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
