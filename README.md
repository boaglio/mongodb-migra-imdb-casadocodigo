mongodb-migra-imdb-casadocodigo
===============================

Programa auxiliar de migração simplificada da base do IMDB (obtida pelo JMDB) para o MongoDB 


Origem:

Banco de dados do Internet Movie Database:

ftp://ftp.fu-berlin.de/pub/misc/movies/database/

Programa JMDB que baixa instala em MySQL:

http://www.jmdb.de/content/eng/download/download.html

Nesse modelo simplicado migramos 7 tabelas:

```
mysql> show tables;
+------------------+
| Tables_in_jmdb   |
+------------------+
| actors           |
| directors        |
| genres           |
| movies           |
| movies2actors    |
| movies2directors |
| ratings          |
+------------------+
7 rows in set (0,00 sec)

mysql> desc actors;
+---------+-----------------------+------+-----+---------+----------------+
| Field   | Type                  | Null | Key | Default | Extra          |
+---------+-----------------------+------+-----+---------+----------------+
| actorid | mediumint(8) unsigned | NO   | PRI | NULL    | auto_increment |
| name    | varchar(250)          | NO   |     | NULL    |                |
| sex     | enum('M','F')         | YES  |     | NULL    |                |
+---------+-----------------------+------+-----+---------+----------------+
3 rows in set (0,00 sec)

mysql> desc directors;
+------------+-----------------------+------+-----+---------+----------------+
| Field      | Type                  | Null | Key | Default | Extra          |
+------------+-----------------------+------+-----+---------+----------------+
| directorid | mediumint(8) unsigned | NO   | PRI | NULL    | auto_increment |
| name       | varchar(250)          | NO   |     | NULL    |                |
+------------+-----------------------+------+-----+---------+----------------+
2 rows in set (0,00 sec)

mysql> desc genres;
+---------+-----------------------+------+-----+---------+-------+
| Field   | Type                  | Null | Key | Default | Extra |
+---------+-----------------------+------+-----+---------+-------+
| movieid | mediumint(8) unsigned | NO   | MUL | NULL    |       |
| genre   | varchar(50)           | NO   |     | NULL    |       |
+---------+-----------------------+------+-----+---------+-------+
2 rows in set (0,00 sec)

mysql> desc movies;
+---------+-----------------------+------+-----+---------+----------------+
| Field   | Type                  | Null | Key | Default | Extra          |
+---------+-----------------------+------+-----+---------+----------------+
| movieid | mediumint(8) unsigned | NO   | PRI | NULL    | auto_increment |
| title   | varchar(400)          | NO   |     | NULL    |                |
| year    | varchar(100)          | YES  |     | NULL    |                |
+---------+-----------------------+------+-----+---------+----------------+
3 rows in set (0,01 sec)

mysql> desc movies2actors;
+---------+-----------------------+------+-----+---------+-------+
| Field   | Type                  | Null | Key | Default | Extra |
+---------+-----------------------+------+-----+---------+-------+
| movieid | mediumint(8) unsigned | NO   | MUL | NULL    |       |
| actorid | mediumint(8) unsigned | NO   | MUL | NULL    |       |
+---------+-----------------------+------+-----+---------+-------+
2 rows in set (0,00 sec)

mysql> desc movies2directors;
+------------+-----------------------+------+-----+---------+-------+
| Field      | Type                  | Null | Key | Default | Extra |
+------------+-----------------------+------+-----+---------+-------+
| movieid    | mediumint(8) unsigned | NO   | MUL | NULL    |       |
| directorid | mediumint(8) unsigned | NO   | MUL | NULL    |       |
| addition   | varchar(1000)         | YES  |     | NULL    |       |
+------------+-----------------------+------+-----+---------+-------+
3 rows in set (0,00 sec)

mysql> desc ratings;
+---------+-----------------------+------+-----+---------+-------+
| Field   | Type                  | Null | Key | Default | Extra |
+---------+-----------------------+------+-----+---------+-------+
| movieid | mediumint(8) unsigned | NO   | MUL | NULL    |       |
| rank    | char(4)               | NO   |     | NULL    |       |
| votes   | mediumint(8) unsigned | YES  |     | NULL    |       |
+---------+-----------------------+------+-----+---------+-------+
3 rows in set (0,01 sec)

mysql> 
```

Para uma collection do MongoDB:

```
db.filmes.find({"titulo": "TRON (1982)"}).pretty();
{
        "_id" : NumberLong(2646365),
        "titulo" : "TRON (1982)",
        "ano" : "1982",
        "nota" : 6.8,
        "votos" : NumberLong(78310),
        "categorias" : [
                "Action",
                "Adventure",
                "Sci-Fi"
        ],
        "diretores" : [
                "Lisberger, Steven"
        ],
        "atores" : [
                {
                        "nome" : "Berns, Gerald",
                        "sexo" : "M"
                },
                {
                        "nome" : "Bostwick, Jackson",
                        "sexo" : "M"
                },
                {
                        "nome" : "Boxleitner, Bruce",
                        "sexo" : "M"
                },
                {
                        "nome" : "Bridges, Jeff (I)",
                        "sexo" : "M"
                },
                {
                        "nome" : "Brubaker, Tony",
                        "sexo" : "M"
                },
                {
                        "nome" : "Cass Sr., David S.",
                        "sexo" : "M"
                },
                {
                        "nome" : "Catlett, Loyd",
                        "sexo" : "M"
                },
                {
                        "nome" : "Chudy, Craig",
                        "sexo" : "M"
                },
                {
                        "nome" : "Cord, Erik",
                        "sexo" : "M"
                },
                {
                        "nome" : "Deadrick Jr., Vince",
                        "sexo" : "M"
                },
                {
                        "nome" : "Dudikoff, Michael",
                        "sexo" : "M"
                },
                {
                        "nome" : "Feck, Rick",
                        "sexo" : "M"
                },
                {
                        "nome" : "Friedman, Richard Bruce",
                        "sexo" : "M"
                },
                {
                        "nome" : "Hughes, Barnard",
                        "sexo" : "M"
                },
                {
                        "nome" : "Jurasik, Peter",
                        "sexo" : "M"
                },
                {
                        "nome" : "Kenworthy, John (I)",
                        "sexo" : "M"
                },
                {
                        "nome" : "Kremer, Lisette",
                        "sexo" : "F"
                },
                {
                        "nome" : "Maren, Jerry",
                        "sexo" : "M"
                },
                {
                        "nome" : "Morgan, Cindy (I)",
                        "sexo" : "F"
                },
                {
                        "nome" : "Neill, Bob",
                        "sexo" : "M"
                },
                {
                        "nome" : "Picerni, Charlie (I)",
                        "sexo" : "M"
                },
                {
                        "nome" : "Sax, Michael",
                        "sexo" : "M"
                },
                {
                        "nome" : "Schatz, Sam",
                        "sexo" : "M"
                },
                {
                        "nome" : "Shor, Dan",
                        "sexo" : "M"
                },
                {
                        "nome" : "Stephano, Tony",
                        "sexo" : "M"
                },
                {
                        "nome" : "Stewart, Mark (I)",
                        "sexo" : "M"
                },
                {
                        "nome" : "Vuilleumier, Pierre",
                        "sexo" : "M"
                },
                {
                        "nome" : "Warner, David (I)",
                        "sexo" : "M"
                },
                {
                        "nome" : "White, Ted (I)",
                        "sexo" : "M"
                }
        ]
}
```
