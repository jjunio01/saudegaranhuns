/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.garanhuns.sg.model.dao.hibernate;

import java.util.List;
import br.edu.ifpe.garanhuns.sg.model.dao.interfaces.BairroDAO;
import br.edu.ifpe.garanhuns.sg.model.pojo.Bairro;
import br.edu.ifpe.garanhuns.sg.model.pojo.PostoSaude;
import br.edu.ifpe.garanhuns.sg.model.pojo.Endereco;
import org.hibernate.Session;
import br.edu.ifpe.garanhuns.sg.util.HibernateUtil;

/**
 *
 * @author Herikles
 */
public class BairroHibernate implements BairroDAO {
    
    @Override
    public void inserir(Bairro o) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.save(o);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.err.println("Falha ao salvar Bairro. Erro: " + e.toString());
        } finally {
            session.close();
        }
    }
    
    @Override
    public void atualizar(Bairro o) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.update(o);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.err.println("Falha ao alterar Bairro. Erro: " + e.toString());
        } finally {
            session.close();
        }
    }
    
    @Override
    public void deletar(Bairro o) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.delete(o);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.err.println("Falha ao remover Bairro. Erro: " + e.toString());
        } finally {
            session.close();
        }
    }
    
    @Override
    public Bairro recuperar(Integer id) {
        Session session = HibernateUtil.getSession();
        try {
            return (Bairro) session.get(Bairro.class.getName(), id);
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.err.println("Falha ao recuperar Bairro. Erro: " + e.toString());
        } finally {
            session.close();
        }
        return null;
    }
    
    @Override
    public List<Bairro> recuperarTodos() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            List<Bairro> lista = session.createQuery("from " + Bairro.class.getName()).getResultList();
            session.getTransaction().commit();
            return lista;
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.err.println("Falha ao recuperar todos os Endereços. Erro: " + e.toString());
        } finally {
            session.close();
        }
        return null;
    }
    
    @Override
    public void deleteOnCascade(Bairro b) {
        //Session session = HibernateUtil.getSession();
        EnderecoHibernate eh = new EnderecoHibernate();
        PostoSaudeHibernate ps = new PostoSaudeHibernate();
        
        List<Endereco> todosEnderecos = eh.recuperarTodos();
        List<PostoSaude> todosPostos = ps.recuperarTodos();
        for (Endereco e : todosEnderecos) {
            if (e.getBairro().getNome().equals(b.getNome())) {
                eh.deleteOnCascade(e);
            }
        }
        for (PostoSaude p : todosPostos) {
            if (p.getEndereco().getLogradouro().equals(b.getNome())) {
                ps.deleteOnCascade(p);
            }
        }
        deletar(b);
    }
    
    @Override
    public Bairro readByName(String name) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            List<Bairro> bairro = (session.createQuery("from " + Bairro.class.getName()).list());
            for (Bairro b : bairro) {
                if (b.getNome().equals(name)) {
                    return b;
                }
            }
            session.getTransaction().commit();
            
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.err.println("Falha ao recuperar o Bairro por nome. Erro: " + e.toString());
        } finally {
            session.close();
        }
        return null;
    }
    
}
