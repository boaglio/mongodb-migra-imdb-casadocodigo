package com.boaglio.casadocodigo.mongodb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.boaglio.casadocodigo.mongodb.dto.Ator;
import com.boaglio.casadocodigo.mongodb.dto.Filme;

public class MySQLDAO {

	public List<Filme> getFilmes() {

		List<Filme> filmes = new ArrayList<Filme>();
		String query = "select m.movieid, m.title, m.year, r.rank, r.votes from movies m left join ratings r  on m.movieid = r.movieid order by 1";
		Statement st = null;
		ResultSet rs = null;
		Connection conexao = null;
		try {
			conexao = getConexao();
			st = conexao.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				Filme filme = new Filme();
				filme.setId(rs.getLong("movieid"));
				filme.setTitulo(rs.getString("title"));
				filme.setAno(rs.getString("year"));
				filme.setNota(rs.getDouble("rank"));
				filme.setVotos(rs.getLong("votes"));
				filmes.add(filme);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (Exception e) {}
			try {
				if (st != null && !st.isClosed()) {
					st.close();
				}
			} catch (Exception e) {}
			try {
				if (conexao != null && !conexao.isClosed()) {
					conexao.close();
				}
			} catch (Exception e) {}
		}
		return filmes;

	}

	public List<String> getCategorias(long movieId) {

		List<String> categorias = new ArrayList<String>();
		String query = "select g.genre from movies m, genres g where m.movieid = g.movieid and m.movieid=? order by 1";

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conexao = null;
		try {
			conexao = getConexao();
			ps = conexao.prepareStatement(query);
			ps.setLong(1,movieId);

			rs = ps.executeQuery();
			while (rs.next()) {
				categorias.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (Exception e) {}
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
			} catch (Exception e) {}
			try {
				if (conexao != null && !conexao.isClosed()) {
					conexao.close();
				}
			} catch (Exception e) {}
		}
		System.out.println("categorias: " + categorias);
		return categorias;

	}

	public List<String> getDiretores(long movieId) {

		List<String> diretores = new ArrayList<String>();
		String query = "select d.name from movies m,  movies2directors md, directors d where m.movieid=md.movieid and d.directorid = md.directorid and m.movieid=? order by 1";

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conexao = null;
		try {
			conexao = getConexao();
			ps = conexao.prepareStatement(query);
			ps.setLong(1,movieId);

			rs = ps.executeQuery();
			while (rs.next()) {
				diretores.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (Exception e) {}
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
			} catch (Exception e) {}
			try {
				if (conexao != null && !conexao.isClosed()) {
					conexao.close();
				}
			} catch (Exception e) {}
		}
		System.out.println("diretores: " + diretores);
		return diretores;

	}

	public List<Ator> getAtores(long movieId) {

		List<Ator> atores = new ArrayList<Ator>();
		String query = "select a.name, a.sex from movies m,  movies2actors ma, actors a where m.movieid=ma.movieid and a.actorid=ma.actorid and m.movieid=? order by 1";

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conexao = null;
		try {
			conexao = getConexao();
			ps = conexao.prepareStatement(query);
			ps.setLong(1,movieId);

			rs = ps.executeQuery();
			while (rs.next()) {
				Ator ator = new Ator();
				ator.setNome(rs.getString(1));
				ator.setSexo(rs.getString(2));
				atores.add(ator);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (Exception e) {}
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
			} catch (Exception e) {}
			try {
				if (conexao != null && !conexao.isClosed()) {
					conexao.close();
				}
			} catch (Exception e) {}
		}
		System.out.println("atores: " + atores);
		return atores;

	}

	private Connection getConexao() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jmdb?user=root&password=temaki111&autoReconnect=true&failOverReadOnly=false&maxReconnects=10");
		} catch (SQLException ex) {

			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}
}
