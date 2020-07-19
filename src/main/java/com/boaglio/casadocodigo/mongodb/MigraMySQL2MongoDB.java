package com.boaglio.casadocodigo.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.boaglio.casadocodigo.mongodb.dto.Ator;
import com.boaglio.casadocodigo.mongodb.dto.Filme;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MigraMySQL2MongoDB {

	static MySQLDAO mysqlDAO = new MySQLDAO();
	static long totalDeFilmes = 0;
	static MongoClient mongoClient;
	static MongoDatabase database;
	static MongoCollection<Document> filmesCollection;
	// padrao
	static final WriteConcern ESCRITA_PADRAO = WriteConcern.ACKNOWLEDGED;
	// otimizacao
	static final WriteConcern ESCRITA_RAPIDA = WriteConcern.UNACKNOWLEDGED;
	
	private static WriteConcern tipoDeEscrita;
	
	private static long tamanhoDaCarga = 1000;
	
	public static void main(String[] args) {

		if (args.length>0 && args[0].equalsIgnoreCase("rapido") ) {
			tipoDeEscrita = ESCRITA_RAPIDA;
			System.out.println("[MongoDB] carga rapida ligada...");
		} else {
			System.out.println("[MongoDB] carga normal...");
			tipoDeEscrita = ESCRITA_PADRAO;
		}
		
		long inicio = System.currentTimeMillis();
		System.out.println("========= INICIO: " + new Date());
		mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost"));
		database = mongoClient.getDatabase("test");
		
		System.out.println("[MongoDB] Removendo collection filmes...");
		filmesCollection = database.getCollection("filmes");
		filmesCollection.drop();

		System.out.println("[MySQL] Buscando filmes...");
		List<Filme> filmes = mysqlDAO.getFilmes();
		totalDeFilmes = filmes.size();
		System.out.println("[MySQL] Total de filmes: "+totalDeFilmes);
		
		System.out.println("Iniciando carga...");
		int contador = 1;
		int contadorCarga = 0;
		List<Filme> listaDeFilmes = new ArrayList<>();
		for (Filme filme : filmes) {
			
			System.out.println("Carregando no cache filme " + contador + " de " + totalDeFilmes + " " + getPorcentagem(contador,totalDeFilmes));
			listaDeFilmes.add(filme);
			
			// faz a carga parcial no MongoDB 
			if ((contadorCarga % tamanhoDaCarga)==0) {				
				adicionarFilmes(listaDeFilmes,tipoDeEscrita);
				listaDeFilmes = new ArrayList<>();
				contadorCarga = 0;
			}
			contador++;
			contadorCarga++;
		}
		adicionarFilmes(listaDeFilmes,tipoDeEscrita);

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

	public static void adicionarFilmes(List<Filme> filmes,WriteConcern tipoDeEscrita) {

		List<Document> listaDocumentos = new ArrayList<>();
		List<Document> atoresMongoDB = new ArrayList<Document>();
		List<Ator> atoresMySQL = new ArrayList<Ator>();
		
		for (Filme filme : filmes) {
			
			Document documento = new Document();
			
			// informacoes basicas
			documento.put("_id",filme.getId());
			documento.put("titulo",filme.getTitulo());
			documento.put("ano",filme.getAno());
			documento.put("nota",filme.getNota());
			documento.put("votos",filme.getVotos());
			
			// categorias 
			documento.put("categorias",mysqlDAO.getCategorias(Long.valueOf(filme.getId())));
			
			// diretores
			documento.put("diretores",mysqlDAO.getDiretores(Long.valueOf(filme.getId())));
			
			// atores
			atoresMongoDB = new ArrayList<Document>();
			atoresMySQL = mysqlDAO.getAtores(Long.valueOf(filme.getId()));
			
			for (Ator ator : atoresMySQL) {
				Document atorMongoDB = new Document();
				atorMongoDB.put("nome",ator.getNome());
				atorMongoDB.put("sexo",ator.getSexo());
				atoresMongoDB.add(atorMongoDB);
			}
			documento.put("atores",atoresMongoDB);

			// adiciona na lista
			listaDocumentos.add(documento);
		}

		System.out.println("[MongoDB] Gravando "+listaDocumentos.size()+" filmes");
		// registra a collection de documentos 
		filmesCollection.withWriteConcern(tipoDeEscrita).insertMany(listaDocumentos);

	}

}
