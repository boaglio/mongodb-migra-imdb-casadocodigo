package com.boaglio.casadocodigo.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boaglio.casadocodigo.mongodb.dto.Ator;
import com.boaglio.casadocodigo.mongodb.dto.Filme;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MigraMySQL2MongoDB {

	static MySQLDAO mysqlDAO = new MySQLDAO();
	static long totalDeFilmes = 0;
	static MongoClient mongoClient;
	static DB blogDatabase;
	static DBCollection filmesCollection;

	public static void main(String[] args) {

		long inicio = System.currentTimeMillis();
		System.out.println("========= INICIO: " + new Date());
		try {
			mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		blogDatabase = mongoClient.getDB("test");
		filmesCollection = blogDatabase.getCollection("filmes");
		filmesCollection.drop();

		System.out.println("Buscando filmes...");
		List<Filme> filmes = mysqlDAO.getFilmes();
		totalDeFilmes = filmes.size();

		System.out.println("Iniciando carga...");
		int contador = 1;
		for (Filme filme : filmes) {
			System.out.println("Carregando filme " + contador + " de " + totalDeFilmes + " " + getPorcentagem(contador,totalDeFilmes));
			adicionarFilme(filme.getId(),filme.getTitulo(),filme.getAno(),filme.getNota(),filme.getVotos());
			contador++;
		}

		double tempoGastoMin = (System.currentTimeMillis() - inicio) / (1000.0 * 60);
		double tempoGastoHr = (System.currentTimeMillis() - inicio) / (1000.0 * 60 * 60);

		System.out.println("========= FIM: " + new Date());
		System.out.println("Tempo gasto em minutos:" + tempoGastoMin);
		System.out.println("Tempo gasto em horas:" + tempoGastoHr);

	}

	private static String getPorcentagem(int contador,long total) {
		long pct = 100 * contador / total;
		return pct + "%";
	}

	public static void adicionarFilme(Long movieid,String title,String year,Double rank,Long votes) {

		BasicDBObject document = new BasicDBObject();

		document.put("_id",movieid);
		document.put("titulo",title);
		document.put("ano",year);
		document.put("nota",rank);
		document.put("votos",votes);
		document.put("categorias",mysqlDAO.getCategorias(Long.valueOf(movieid)));
		document.put("diretores",mysqlDAO.getDiretores(Long.valueOf(movieid)));

		List<BasicDBObject> atoresMongoDB = new ArrayList<BasicDBObject>();
		List<Ator> atoresMySQL = mysqlDAO.getAtores(Long.valueOf(movieid));

		for (Ator ator : atoresMySQL) {
			BasicDBObject atorMongoDB = new BasicDBObject();
			atorMongoDB.put("nome",ator.getNome());
			atorMongoDB.put("sexo",ator.getSexo());
			atoresMongoDB.add(atorMongoDB);
		}

		document.put("atores",atoresMongoDB);

		filmesCollection.insert(document);

	}

}
