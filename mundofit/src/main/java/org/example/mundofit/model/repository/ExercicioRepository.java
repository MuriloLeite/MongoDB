package org.example.mundofit.model.repository;

import com.mongodb.BasicDBObject;
import org.example.mundofit.model.Exercicio;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Date;

public interface ExercicioRepository extends MongoRepository<Exercicio, String> {
    /**
     * Encontra exercícios pelo ID do aluno.
     * @param alunoId ID do aluno.
     * @return Lista de exercícios realizados pelo aluno com o ID fornecido.
     */
    List<Exercicio> findByAlunoId(String alunoId);

    /**
     * Encontra exercícios dentro de um intervalo de datas.
     * @param dataInicio Data de início do intervalo.
     * @param dataFim Data de fim do intervalo.
     * @return Lista de exercícios realizados entre as datas fornecidas.
     */
    @Query("{ 'dataExercicio' : { $gte: ?0, $lt: ?1 }}")
    List<Exercicio> findByDataExercicio(Date dataInicio, Date dataFim);

    /**
     * Encontra exercícios com uma intensidade específica e duração maior que o valor fornecido.
     * @param intensidade Intensidade do exercício (e.g., leve, moderado, intenso).
     * @param duracaoMinutos Duração mínima do exercício em minutos.
     * @return Lista de exercícios que correspondem à intensidade e duração fornecidas.
     */
    @Query("{ 'intensidade' : ?0, 'duracaoMinutos' : { $gt: ?1 } }")
    List<Exercicio> findByIntensidadeAndDuracaoGreaterThan(String intensidade, int duracaoMinutos);

    /**
     * Encontra todos os exercícios retornando apenas a duração de cada um.
     * @return Lista de exercícios com apenas o campo de duração preenchido.
     */
    @Query(value = "{}", fields = "{'duracaoMinutos' : 1}")
    List<Exercicio> findAllDurations();

    /**
     * Encontra todos os exercícios retornando o ID do aluno, duração e nome do exercício.
     * @return Lista de exercícios com os campos ID do aluno, duração e nome do exercício preenchidos.
     */
    @Query(value = "{}", fields = "{'alunoId' : 1, 'duracaoMinutos' : 1, 'nome' : 1}")
    List<Exercicio> findAllDurationsAndAluno();

    /**
     * Encontra exercícios realizados dentro de um intervalo de datas.
     * @param startDate Data de início do intervalo.
     * @param endDate Data de fim do intervalo.
     * @return Lista de exercícios realizados entre as datas fornecidas.
     */
    @Query("{ 'dataExercicio' : { $gte: ?0, $lt: ?1 }}")
    List<Exercicio> findByDataExercicioBetween(Date startDate, Date endDate);

    /**
     * Encontra exercícios realizados por alunos cujos IDs estão na lista fornecida.
     * @param alunoIds Lista de IDs de alunos.
     * @return Lista de exercícios realizados pelos alunos cujos IDs estão na lista.
     */
    @Query("{'alunoId' : { $in: ?0 }}")
    List<Exercicio> findByAlunoIdIn(List<String> alunoIds);

    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'alunos', 'localField': 'alunoId', 'foreignField': '_id', 'as': 'aluno' } }",
            "{ '$unwind': '$aluno' }",
            "{ '$match': { 'aluno.nome': { '$regex': '^?0', '$options': 'i' } } }",
            "{ '$project': { 'alunoId': 1, 'nomeExercicio': 1, 'intensidade': 1, 'duracaoMinutos': 1, 'dataExercicio': 1 } }"
    })
    List<Exercicio> findExercisesByStudentNameStartingWith(String letraInicial);

    /**
     * Encontra exercícios realizados após a data de inscrição dos alunos.
     * @param dataInscricao Data de inscrição.
     * @return Lista de exercícios realizados após a data de inscrição fornecida.
     */
    @Query("{ 'alunoId' : { $exists : true }, 'dataExercicio' : { $gte: ?0 }}")
    List<Exercicio> findByAlunoInscritoAposData(Date dataInscricao);

    /**
     * Calcula o tempo total de exercício por aluno, ordenado do maior para o menor.
     * @return Lista de documentos contendo o ID do aluno e o tempo total de exercício.
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$alunoId', 'totalDuracao': { '$sum': '$duracaoMinutos' } } }",
            "{ '$sort': { 'totalDuracao': -1 } }"
    })
    List<BasicDBObject> calculateTotalExerciseTimeByStudent();

    /**
     * Calcula a média de duração dos exercícios por intensidade.
     * @return Lista de documentos contendo a intensidade e a média de duração dos exercícios.
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$intensidade', 'mediaDuracao': { '$avg': '$duracaoMinutos' } } }",
            "{ '$sort': { '_id': 1 } }"
    })
    List<BasicDBObject> calculateAverageDurationByIntensity();



    /**
     * Calcula a distribuição de exercícios por idade dos alunos.
     * Utiliza uma agregação no banco de dados para calcular quantos exercícios foram realizados por faixa etária,
     * agrupando os alunos em categorias: Menor que 30 anos, 30-50 anos e Maior que 50 anos.
     *
     * @return Lista de documentos contendo a faixa etária e o total de exercícios realizados por essa faixa.
     */
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'alunos', 'localField': 'alunoId', 'foreignField': '_id', 'as': 'aluno' } }",
            "{ '$unwind': '$aluno' }",
            "{ '$addFields': { 'aluno.idadeEmMs': { '$subtract': [ { '$toLong': '$$NOW' }, { '$toLong': '$aluno.dataNascimento' } ] } } }",
            "{ '$addFields': { 'aluno.idade': { '$divide': [ '$aluno.idadeEmMs', 31536000000 ] } } }",
            "{ '$group': { '_id': { '$cond': [ { '$lt': [ '$aluno.idade', 30 ] }, 'Menor que 30', { '$cond': [ { '$lt': [ '$aluno.idade', 50 ] }, '30-50', 'Maior que 50' ] } ] }, 'totalExercicios': { '$sum': 1 } } }"
    })
    List<BasicDBObject> calculateExerciseDistributionByAge();


    /**
     * Conta o número de exercícios realizados por mês para cada aluno.
     * Utiliza uma agregação para agrupar os exercícios por mês e ano de realização,
     * e conta quantos exercícios cada aluno realizou em cada período.
     *
     * @return Lista de documentos contendo o nome do aluno, mês, ano e o total de exercícios realizados.
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': { 'alunoId': '$alunoId', 'mes': { '$month': '$dataExercicio' }, 'ano': { '$year': '$dataExercicio' } }, 'totalExercicios': { '$sum': 1 } } }",
            "{ '$lookup': { 'from': 'alunos', 'localField': '_id.alunoId', 'foreignField': '_id', 'as': 'aluno' } }",
            "{ '$unwind': { 'path': '$aluno', 'preserveNullAndEmptyArrays': true } }",
            "{ '$project': { 'nomeAluno': { '$ifNull': ['$aluno.nome', 'Desconhecido'] }, 'mes': '$_id.mes', 'ano': '$_id.ano', 'totalExercicios': 1, '_id': 0 } }",
            "{ '$sort': { 'ano': 1, 'mes': 1, 'nomeAluno': 1 } }"
    })
    List<BasicDBObject> countExercisesPerMonthByStudent();


    /**
     * Encontra os melhores alunos de cada mês com base no número total de exercícios realizados.
     * Utiliza uma agregação para encontrar os alunos que realizaram o maior número de exercícios em cada mês,
     * e retorna uma lista ordenada por ano, mês e nome do aluno.
     *
     * @return Lista de documentos contendo o nome do aluno, mês, ano e o total de exercícios realizados, dos melhores alunos de cada mês.
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': { 'alunoId': '$alunoId', 'mes': { '$month': '$dataExercicio' }, 'ano': { '$year': '$dataExercicio' } }, 'totalExercicios': { '$sum': 1 } } }",
            "{ '$sort': { '_id.mes': 1, 'totalExercicios': -1 } }",
            "{ '$group': { '_id': { 'mes': '$_id.mes', 'ano': '$_id.ano' }, 'alunoId': { '$first': '$_id.alunoId' }, 'totalExercicios': { '$first': '$totalExercicios' } } }",
            "{ '$lookup': { 'from': 'alunos', 'localField': 'alunoId', 'foreignField': '_id', 'as': 'aluno' } }",
            "{ '$unwind': { 'path': '$aluno', 'preserveNullAndEmptyArrays': true } }",
            "{ '$project': { 'nomeAluno': { '$ifNull': ['$aluno.nome', 'Desconhecido'] }, 'mes': '$_id.mes', 'ano': '$_id.ano', 'totalExercicios': 1, '_id': 0 } }",
            "{ '$sort': { 'ano': 1, 'mes': 1, 'nomeAluno': 1 } }"
    })
    List<BasicDBObject> findTopStudentsByMonth();


    /**
     * Conta o número de exercícios realizados por intensidade e mês.
     * Utiliza uma agregação para agrupar os exercícios por intensidade, mês e ano de realização,
     * e conta quantos exercícios foram realizados para cada combinação de intensidade e período.
     *
     * @return Lista de documentos contendo a intensidade, mês, ano e o total de exercícios realizados.
     */
    @Aggregation(pipeline = {
            "{ '$group': { '_id': { 'intensidade': '$intensidade', 'mes': { '$month': '$dataExercicio' }, 'ano': { '$year': '$dataExercicio' } }, 'totalExercicios': { '$sum': 1 } } }",
            "{ '$project': { 'intensidade': '$_id.intensidade', 'mes': '$_id.mes', 'ano': '$_id.ano', 'totalExercicios': 1, '_id': 0 } }",
            "{ '$sort': { 'ano': 1, 'mes': 1, 'intensidade': 1 } }"
    })
    List<BasicDBObject> countExercisesByIntensityAndMonth();

}

