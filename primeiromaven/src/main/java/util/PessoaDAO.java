package util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import entidade.EPessoa;

public class PessoaDAO {
	
	private static PessoaDAO instance;
	protected EntityManager entityManager;

	public static PessoaDAO getInstance() {
		if (instance == null) {
			instance = new PessoaDAO();
		}

		return instance;
	}

	private PessoaDAO() {
		entityManager = getEntityManager();
	}

	private EntityManager getEntityManager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("unit-jsf");
		if (entityManager == null) {
			entityManager = factory.createEntityManager();
		}

		return entityManager;
	}



	public void salvar(EPessoa pessoa) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(pessoa);
			entityManager.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}
	
	
	public EPessoa buscarPorID(final long id) {
		return entityManager.find(EPessoa.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<EPessoa> listarTodos() {
		return entityManager.createQuery("FROM " + EPessoa.class.getName()).getResultList();
	}


	public void alterar(EPessoa pessoa) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(pessoa);
			entityManager.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

	public void remover(EPessoa pessoa) {
		try {
			entityManager.getTransaction().begin();
			pessoa = entityManager.find(EPessoa.class, pessoa.getId());
			entityManager.remove(pessoa);
			entityManager.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

	public void removerPorID(final long id) {
		try {
			EPessoa pessoa = buscarPorID(id);
			remover(pessoa);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}