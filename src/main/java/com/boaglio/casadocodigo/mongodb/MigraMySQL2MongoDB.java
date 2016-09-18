package com.boaglio.casadocodigo.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.boaglio.casadocodigo.mongodb.dto.Ator;
import com.boaglio.casadocodigo.mongodb.dto.Filme;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MigraMySQL2MongoDB {

	static MySQLDAO mysqlDAO = new MySQLDAO();
	static long totalDeFilmes = 0;
	static MongoClient mongoClient;
	static MongoDatabase database;
	static MongoCollection<Document> filmesCollection;

	public static void main(String[] args) {

		long inicio = System.currentTimeMillis();
		System.out.println("========= INICIO: " + new Date());
		mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost"));
		database = mongoClient.getDatabase("test");
		filmesCollection = database.getCollection("filmes");
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

		Document documento = new Document();

		documento.put("_id",movieid);
		documento.put("titulo",title);
		documento.put("ano",year);
		documento.put("nota",rank);
		documento.put("votos",votes);
		documento.put("categorias",mysqlDAO.getCategorias(Long.valueOf(movieid)));
		documento.put("diretores",mysqlDAO.getDiretores(Long.valueOf(movieid)));

		List<Document> atoresMongoDB = new ArrayList<Document>();
		List<Ator> atoresMySQL = mysqlDAO.getAtores(Long.valueOf(movieid));

		for (Ator ator : atoresMySQL) {
			Document atorMongoDB = new Document();
			atorMongoDB.put("nome",ator.getNome());
			atorMongoDB.put("sexo",ator.getSexo());
			atoresMongoDB.add(atorMongoDB);
		}

		documento.put("atores",atoresMongoDB);

		filmesCollection.insertOne(documento);

	}

}
