package org.example.mundofit;

import com.mongodb.BasicDBObject;
import org.example.mundofit.model.Aluno;
import org.example.mundofit.model.Dieta;
import org.example.mundofit.model.Exercicio;
import org.example.mundofit.model.repository.AlunoRepository;
import org.example.mundofit.model.repository.DietaRepository;
import org.example.mundofit.model.repository.ExercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class MundofitApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MundofitApplication.class, args);
    }

    @Autowired
    AlunoRepository alunoRepo;

    @Autowired
    ExercicioRepository exercicioRepo;

    @Autowired
    DietaRepository dietaRepo;

    @Override
    public void run(String... args) {
        System.out.println("### SERVER STARTED ###");

        Scanner le = new Scanner(System.in);

        while (true) {
            try {
                int escolha = exibeMenu(le);
                switch (escolha) {
                    case 1 -> cadastrarAluno(le);
                    case 2 -> atualizarAluno(le);
                    case 3 -> removerAluno(le);
                    case 4 -> cadastrarExercicio(le);
                    case 5 -> atualizarExercicio(le);
                    case 6 -> removerExercicio(le);
                    case 7 -> realizarConsultas(le);
                    case 8 -> cadastrarDieta(le);
                    case 9 -> consultarDietas(le);
                    case 10 -> {
                        System.out.println("Saindo...");
                        return;
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Ocorreu um erro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void cadastrarAluno(Scanner le) {
        try {
            System.out.println("#############################");
            System.out.println("########  ALUNO  #########");
            System.out.println("#############################");
            Aluno aluno = new Aluno();
            System.out.println("# Informe o ID do aluno:");
            aluno.setId(le.next());
            System.out.println("# Informe o nome do aluno:");
            aluno.setNome(le.next());
            System.out.println("# Informe o sexo do aluno:");
            aluno.setSexo(le.next());
            System.out.println("# Informe a data de nascimento (yyyy-MM-dd):");
            aluno.setDataNascimento(java.sql.Date.valueOf(le.next()));
            aluno.setDataInscricao(new Date());

            alunoRepo.save(aluno);
            System.out.println("Aluno cadastrado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar aluno: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarAluno(Scanner le) {
        try {
            System.out.println("#############################");
            System.out.println("#######  ATUALIZAR ALUNO  ########");
            System.out.println("#############################");
            System.out.println("# Informe o ID do aluno a ser atualizado:");
            String id = le.next();
            Optional<Aluno> alunoOpt = alunoRepo.findById(id);

            if (alunoOpt.isPresent()) {
                Aluno aluno = alunoOpt.get();
                System.out.println("# Informe o novo nome do aluno:");
                aluno.setNome(le.next());
                System.out.println("# Informe o novo sexo do aluno:");
                aluno.setSexo(le.next());
                System.out.println("# Informe a nova data de nascimento (yyyy-MM-dd):");
                aluno.setDataNascimento(java.sql.Date.valueOf(le.next()));

                alunoRepo.save(aluno);
                System.out.println("Aluno atualizado com sucesso!");
            } else {
                System.out.println("Aluno não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar aluno: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void removerAluno(Scanner le) {
        try {
            System.out.println("#############################");
            System.out.println("########  REMOVER ALUNO  #########");
            System.out.println("#############################");
            System.out.println("# Informe o ID do aluno a ser removido:");
            String id = le.next();
            alunoRepo.deleteById(id);
            System.out.println("Aluno removido com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao remover aluno: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cadastrarExercicio(Scanner le) {
        try {
            System.out.println("#############################");
            System.out.println("#######  EXERCICIO  #########");
            System.out.println("#############################");
            Exercicio exercicio = new Exercicio();
            System.out.println("# Informe o ID do exercício:");
            exercicio.setId(le.next());
            System.out.println("# Informe o ID do aluno:");
            exercicio.setAlunoId(le.next());
            System.out.println("# Informe o nome do exercício:");
            exercicio.setNomeExercicio(le.next());
            System.out.println("# Informe a intensidade do exercício (leve, moderado, intenso):");
            exercicio.setIntensidade(le.next());
            System.out.println("# Informe a duração em minutos:");
            exercicio.setDuracaoMinutos(le.nextInt());
            exercicio.setDataExercicio(new Date());

            exercicioRepo.save(exercicio);
            System.out.println("Exercício cadastrado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar exercício: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarExercicio(Scanner le) {
        try {
            System.out.println("#############################");
            System.out.println("#######  ATUALIZAR EXERCICIO  #########");
            System.out.println("#############################");
            System.out.println("# Informe o ID do exercício a ser atualizado:");
            String id = le.next();
            Optional<Exercicio> exercicioOpt = exercicioRepo.findById(id);

            if (exercicioOpt.isPresent()) {
                Exercicio exercicio = exercicioOpt.get();
                System.out.println("# Informe o novo nome do exercício:");
                exercicio.setNomeExercicio(le.next());
                System.out.println("# Informe a nova intensidade do exercício:");
                exercicio.setIntensidade(le.next());
                System.out.println("# Informe a nova duração em minutos:");
                exercicio.setDuracaoMinutos(le.nextInt());

                exercicioRepo.save(exercicio);
                System.out.println("Exercício atualizado com sucesso!");
            } else {
                System.out.println("Exercício não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar exercício: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void removerExercicio(Scanner le) {
        try {
            System.out.println("#############################");
            System.out.println("########  REMOVER EXERCICIO  #########");
            System.out.println("#############################");
            System.out.println("# Informe o ID do exercício a ser removido:");
            String id = le.next();
            exercicioRepo.deleteById(id);
            System.out.println("Exercício removido com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao remover exercício: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void cadastrarDieta(Scanner le) {
        try {
            System.out.println("#############################");
            System.out.println("########  DIETA  #########");
            System.out.println("#############################");
            Dieta dieta = new Dieta();
            System.out.println("# Informe o ID da dieta:");
            dieta.setId(le.next());
            System.out.println("# Informe o ID do aluno:");
            dieta.setAlunoId(le.next());
            System.out.println("# Descreva a dieta:");
            le.nextLine(); // Consumir a linha pendente
            dieta.setDescricao(le.nextLine());
            dieta.setDataCadastro(new Date());

            dietaRepo.save(dieta);
            System.out.println("Dieta cadastrada com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar dieta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void consultarDietas(Scanner le) {
        try {
            System.out.println("#############################");
            System.out.println("#######  CONSULTAR DIETAS  ########");
            System.out.println("#############################");
            List<Dieta> dietas = dietaRepo.findAll();
            dietas.forEach(dieta -> {
                System.out.println("ID da Dieta: " + dieta.getId());
                System.out.println("ID do Aluno: " + dieta.getAlunoId());
                System.out.println("Descrição: " + dieta.getDescricao());
                System.out.println("Data de Cadastro: " + dieta.getDataCadastro());
                System.out.println("---------------------------");
            });
        } catch (Exception e) {
            System.err.println("Erro ao consultar dietas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void imprimirDetalhesExercicio(Exercicio exercicio) {
        System.out.println("Exercício: " + exercicio.getNomeExercicio());
        System.out.println("ID do Aluno: " + exercicio.getAlunoId());
        System.out.println("Intensidade: " + exercicio.getIntensidade());
        System.out.println("Duração: " + exercicio.getDuracaoMinutos() + " minutos");
        System.out.println("Data do Exercício: " + exercicio.getDataExercicio());
        System.out.println("---------------------------");
    }


    private void realizarConsultas(Scanner le) {
        System.out.println("#############################");
        System.out.println("#######  CONSULTAS  #########");
        System.out.println("#############################");
        System.out.println("Escolha a consulta desejada:");
        System.out.println("1) Exercícios por Aluno");
        System.out.println("2) Exercícios por Data");
        System.out.println("3) Exercícios Intensos com Duração > 10 minutos");
        System.out.println("4) Média de Duração dos Exercícios");
        System.out.println("5) Aluno com Maior Tempo de Exercício");
        System.out.println("6) Alunos com Exercícios em um Período");
        System.out.println("7) Exercícios por Sexo do Aluno");
        System.out.println("8) Total de Exercícios por Aluno");
        System.out.println("9) Média de Duração por Intensidade");
        System.out.println("10) Exercícios de Alunos com Idade > 30 anos");
        System.out.println("11) Exercícios após Data de Inscrição");
        System.out.println("12) Exercícios por Nome Inicial do Aluno");
        System.out.println("13) Contar o número de exercícios realizados por dia na última semana");
        System.out.println("14) Exibir o exercício mais comum realizado pelos alunos");
        System.out.println("15) Obter a lista de alunos que realizaram exercícios de todos os níveis de intensidade");
        System.out.println("16) Tempo Total de Exercício por Aluno");
        System.out.println("17) Distribuição de Exercícios por Idade dos Alunos");
        System.out.println("18) Calcular o total de exercícios realizados por cada aluno em cada mês");
        System.out.println("19) Encontrar os alunos que realizaram mais exercícios em cada mês");
        System.out.println("20) Exercícios por Tipo de Intensidade e Mês");

        int opcao = le.nextInt();
        le.nextLine();

        try {
            switch (opcao) {
                case 1:
                    // Consulta: Exercícios por Aluno
                    System.out.println("Informe o ID do aluno:");
                    String alunoId = le.nextLine();
                    List<Exercicio> exerciciosAluno = exercicioRepo.findByAlunoId(alunoId);
                    exerciciosAluno.forEach(this::imprimirDetalhesExercicio);
                    break;


                case 2:
                    // Consulta: Exercícios por Data
                    System.out.println("Informe a data (yyyy-MM-dd):");
                    String dataStr = le.nextLine();
                    Date data = java.sql.Date.valueOf(dataStr);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(data);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    Date dataFim = calendar.getTime();
                    List<Exercicio> exerciciosData = exercicioRepo.findByDataExercicio(data, dataFim);
                    if (exerciciosData.isEmpty()) {
                        System.out.println("Nenhum exercício encontrado para a data: " + dataStr);
                    } else {
                        exerciciosData.forEach(this::imprimirDetalhesExercicio);
                    }
                    break;


                case 3:
                    // Consulta: Exercícios Intensos com Duração > 10 minutos
                    List<Exercicio> exerciciosIntensos = exercicioRepo.findByIntensidadeAndDuracaoGreaterThan("intenso", 10);
                    exerciciosIntensos.forEach(this::imprimirDetalhesExercicio);
                    break;


                case 4:
                    // Consulta: Média de Duração dos Exercícios
                    List<Exercicio> todosExercicios = exercicioRepo.findAllDurations();
                    double mediaDuracao = todosExercicios.stream()
                            .mapToInt(Exercicio::getDuracaoMinutos)
                            .average()
                            .orElse(0);
                    System.out.println("Média de duração dos exercícios: " + mediaDuracao);
                    break;


                case 5:
                    // Consulta: Aluno com Maior Tempo de Exercício
                    List<Exercicio> exerciciosPorAluno = exercicioRepo.findAllDurationsAndAluno();
                    Map<String, Integer> tempoPorAluno = exerciciosPorAluno.stream()
                            .collect(Collectors.groupingBy(Exercicio::getAlunoId, Collectors.summingInt(Exercicio::getDuracaoMinutos)));
                    Optional<Map.Entry<String, Integer>> alunoMaiorTempo = tempoPorAluno.entrySet().stream()
                            .max(Map.Entry.comparingByValue());
                    if (alunoMaiorTempo.isPresent()) {
                        System.out.println("Aluno com maior tempo de exercícios: " + alunoMaiorTempo.get().getKey());
                    } else {
                        System.out.println("Nenhum aluno encontrado.");
                    }
                    break;


                case 6:
                    // Consulta: Alunos com Exercícios em um Período de Tempo
                    System.out.println("Informe a data inicial (yyyy-MM-dd):");
                    String dataInicialStr = le.nextLine();
                    Date dataInicial = java.sql.Date.valueOf(dataInicialStr);

                    System.out.println("Informe a data final (yyyy-MM-dd):");
                    String dataFinalStr = le.nextLine();
                    Date dataFinal = java.sql.Date.valueOf(dataFinalStr);

                    Calendar calendarFinal = Calendar.getInstance();
                    calendarFinal.setTime(dataFinal);
                    calendarFinal.add(Calendar.DAY_OF_MONTH, 1);
                    Date dataFinalAjustada = calendarFinal.getTime();

                    List<Exercicio> exerciciosPeriodo = exercicioRepo.findByDataExercicioBetween(dataInicial, dataFinalAjustada);

                    if (exerciciosPeriodo.isEmpty()) {
                        System.out.println("Nenhum exercício encontrado para o período de " + dataInicialStr + " a " + dataFinalStr);
                    } else {
                        exerciciosPeriodo.forEach(exercicio -> {
                            imprimirDetalhesExercicio(exercicio);
                        });
                    }
                    break;


                case 7:
                    // Consulta: Exercícios por Sexo do Aluno
                    System.out.println("Informe o sexo do aluno (M/F):");
                    String sexo = le.nextLine();
                    List<Aluno> alunosSexo = alunoRepo.findBySexo(sexo);
                    List<String> alunosIds = alunosSexo.stream()
                            .map(Aluno::getId)
                            .collect(Collectors.toList());
                    List<Exercicio> exerciciosSexo = exercicioRepo.findByAlunoIdIn(alunosIds);
                    exerciciosSexo.forEach(this::imprimirDetalhesExercicio);
                    break;


                case 8:
                    // Consulta: Total de Exercícios por Aluno
                    List<Exercicio> exercicios = exercicioRepo.findAllDurationsAndAluno();
                    Map<String, Long> totalExerciciosPorAluno = exercicios.stream()
                            .collect(Collectors.groupingBy(Exercicio::getAlunoId, Collectors.counting()));
                    totalExerciciosPorAluno.forEach((aluno, totalExercicios) -> {
                        System.out.println("Aluno ID: " + aluno + " - Total de exercícios: " + totalExercicios);
                    });
                    break;


                case 9:
                    // Consulta: Média de Duração dos Exercícios por Intensidade
                    List<BasicDBObject> mediaDuracaoPorIntensidadeResult = exercicioRepo.calculateAverageDurationByIntensity();
                    mediaDuracaoPorIntensidadeResult.forEach(result -> {
                        String intensidade = result.getString("_id");
                        Double mediaDuracaoResult = result.getDouble("mediaDuracao");
                        System.out.println("Intensidade: " + intensidade);
                        System.out.println("Média de Duração: " + mediaDuracaoResult + " minutos");
                        System.out.println("---------------------------");
                    });
                    break;


                case 10:
                    // Consulta: Exercícios de Alunos com Idade > 30 anos
                    List<Aluno> todosAlunos = alunoRepo.findAll();
                    LocalDate hoje = LocalDate.now();
                    List<Aluno> alunosMaisDe30 = todosAlunos.stream()
                            .filter(aluno -> {
                                LocalDate dataNascimento = aluno.getDataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                return Period.between(dataNascimento, hoje).getYears() > 30;
                            })
                            .collect(Collectors.toList());
                    List<String> alunosIdsMaisDe30 = alunosMaisDe30.stream().map(Aluno::getId).collect(Collectors.toList());
                    List<Exercicio> exerciciosAlunosIdade = exercicioRepo.findByAlunoIdIn(alunosIdsMaisDe30);
                    exerciciosAlunosIdade.forEach(this::imprimirDetalhesExercicio);
                    break;


                case 11:
                    // Consulta: Exercícios após Data de Inscrição
                    System.out.println("Informe a data de inscrição (yyyy-MM-dd):");
                    String dataInscricaoStr = le.nextLine();
                    Date dataInscricao = java.sql.Date.valueOf(dataInscricaoStr);
                    List<Exercicio> exerciciosAposData = exercicioRepo.findByAlunoInscritoAposData(dataInscricao);
                    exerciciosAposData.forEach(this::imprimirDetalhesExercicio);
                    break;


                case 12:
                    // Consulta: Exercícios por Nome Inicial do Aluno
                    System.out.println("Informe a letra inicial do nome do aluno:");
                    String letraInicial = le.nextLine().toUpperCase(); // Transforma para maiúsculo para garantir compatibilidade

                    List<Exercicio> exerciciosPorNomeAluno = exercicioRepo.findExercisesByStudentNameStartingWith("^" + letraInicial);

                    if (exerciciosPorNomeAluno.isEmpty()) {
                        System.out.println("Nenhum exercício encontrado para alunos cujo nome começa com '" + letraInicial + "'");
                    } else {
                        System.out.println("Exercícios realizados por alunos cujo nome começa com '" + letraInicial + "':");
                        exerciciosPorNomeAluno.forEach(this::imprimirDetalhesExercicio);
                    }
                    break;


                case 13:
                    // Consulta: Contar o número de exercícios realizados por dia na última semana
                    LocalDate dataHoje = LocalDate.now();
                    LocalDate umaSemanaAtras = dataHoje.minusWeeks(1);
                    Map<LocalDate, Long> exerciciosPorDia = new HashMap<>();
                    for (LocalDate dataLoop = umaSemanaAtras; !dataLoop.isAfter(dataHoje); dataLoop = dataLoop.plusDays(1)) {
                        LocalDate dataInicioLoop = dataLoop;
                        LocalDate dataFimLoop = dataLoop.plusDays(1);
                        long totalExercicios = exercicioRepo.findByDataExercicioBetween(
                                Date.from(dataInicioLoop.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                Date.from(dataFimLoop.atStartOfDay(ZoneId.systemDefault()).toInstant())
                        ).size();
                        exerciciosPorDia.put(dataLoop, totalExercicios);
                    }
                    System.out.println("Número de exercícios realizados por dia na última semana:");
                    exerciciosPorDia.forEach((dataLoop, total) -> System.out.println(dataLoop + ": " + total));
                    break;


                case 14:
                    // Consulta: Exibir o exercício mais comum realizado pelos alunos
                    List<Exercicio> exerciciosMaisComuns = exercicioRepo.findAll();
                    Map<String, Long> exerciciosContados = exerciciosMaisComuns.stream()
                            .collect(Collectors.groupingBy(Exercicio::getNomeExercicio, Collectors.counting()));
                    String exercicioMaisComumNome = exerciciosContados.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse("Nenhum exercício encontrado.");
                    System.out.println("Exercício mais comum realizado pelos alunos: " + exercicioMaisComumNome);
                    break;


                case 15:
                    // Consulta: Obter a lista de alunos que realizaram exercícios de todos os níveis de intensidade
                    List<String> niveisIntensidade = Arrays.asList("leve", "moderado", "intenso");
                    Map<String, Set<String>> alunosPorNivel = new HashMap<>();
                    for (String intensidade : niveisIntensidade) {
                        List<Exercicio> exerciciosPorIntensidade = exercicioRepo.findByIntensidadeAndDuracaoGreaterThan(intensidade, 0);
                        Set<String> idsAlunos = exerciciosPorIntensidade.stream()
                                .map(Exercicio::getAlunoId)
                                .collect(Collectors.toSet());
                        alunosPorNivel.put(intensidade, idsAlunos);
                    }
                    Set<String> alunosComTodosNiveis = new HashSet<>(alunosPorNivel.get(niveisIntensidade.get(0)));
                    alunosPorNivel.values().forEach(alunosComTodosNiveis::retainAll);
                    List<Aluno> alunosTodosNiveis = alunoRepo.findAllById(alunosComTodosNiveis);
                    if (alunosTodosNiveis.isEmpty()) {
                        System.out.println("Nenhum aluno encontrado.");
                    } else {
                        System.out.println("Lista de alunos que realizaram exercícios de todos os níveis de intensidade:");
                        alunosTodosNiveis.forEach(aluno -> System.out.println("Nome: " + aluno.getNome() + ", ID: " + aluno.getId()));
                    }
                    break;


                case 16:
                    //implementação agregate
                    // Consulta: Tempo Total de Exercício por Aluno
                    List<BasicDBObject> tempoTotalExercicioPorAluno = exercicioRepo.calculateTotalExerciseTimeByStudent();
                    tempoTotalExercicioPorAluno.forEach(resultado -> {
                        String alunoIdResult = resultado.getString("_id");
                        Integer totalDuracao = resultado.getInt("totalDuracao");
                        Optional<Aluno> alunoOptional = alunoRepo.findById(alunoIdResult);
                        if (alunoOptional.isPresent()) {
                            Aluno aluno = alunoOptional.get();
                            System.out.println("Aluno: " + aluno.getNome() + " (ID: " + alunoIdResult + ")");
                            System.out.println("Tempo Total de Exercício: " + totalDuracao + " minutos");
                            System.out.println("---------------------------");
                        } else {
                            System.out.println("Aluno ID: " + alunoIdResult + " - Tempo Total de Exercício: " + totalDuracao + " minutos");
                            System.out.println("Aviso: Aluno com ID " + alunoIdResult + " não encontrado no repositório de alunos.");
                            System.out.println("---------------------------");
                        }
                    });
                    break;
                case 17:
                    // Consulta: Distribuição de Exercícios por Idade dos Alunos
                    List<BasicDBObject> distribuicaoExerciciosPorIdade = exercicioRepo.calculateExerciseDistributionByAge();
                    if (distribuicaoExerciciosPorIdade.isEmpty()) {
                        System.out.println("Nenhum exercício encontrado.");
                    } else {
                        distribuicaoExerciciosPorIdade.forEach(result -> {
                            String faixaEtaria = result.getString("_id");
                            Integer totalExercicios = result.getInt("totalExercicios");
                            System.out.println("Faixa Etária: " + faixaEtaria);
                            System.out.println("Total de Exercícios: " + totalExercicios);
                            System.out.println("---------------------------");
                        });
                    }
                    break;
                case 18:
                    // Consulta: Calcular o total de exercícios realizados por cada aluno em cada mês
                    List<BasicDBObject> totalExerciciosPorMesPorAluno = exercicioRepo.countExercisesPerMonthByStudent();
                    if (totalExerciciosPorMesPorAluno.isEmpty()) {
                        System.out.println("Nenhum exercício encontrado.");
                    } else {
                        totalExerciciosPorMesPorAluno.forEach(result -> {
                            String nomeAluno = result.getString("nomeAluno");
                            int mes = result.getInt("mes");
                            int ano = result.getInt("ano");
                            int totalExercicios = result.getInt("totalExercicios");
                            System.out.println("Aluno: " + nomeAluno);
                            System.out.println("Mês: " + mes + "/" + ano);
                            System.out.println("Total de Exercícios: " + totalExercicios);
                            System.out.println("---------------------------");
                        });
                    }
                    break;
                case 19:
                    // Consulta: Encontrar os alunos que realizaram mais exercícios em cada mês
                    List<BasicDBObject> topAlunosPorMes = exercicioRepo.findTopStudentsByMonth();
                    if (topAlunosPorMes.isEmpty()) {
                        System.out.println("Nenhum exercício encontrado.");
                    } else {
                        topAlunosPorMes.forEach(result -> {
                            String nomeAluno = result.getString("nomeAluno");
                            int mes = result.getInt("mes");
                            int ano = result.getInt("ano");
                            int totalExercicios = result.getInt("totalExercicios");
                            System.out.println("Aluno: " + nomeAluno);
                            System.out.println("Mês: " + mes + "/" + ano);
                            System.out.println("Total de Exercícios: " + totalExercicios);
                            System.out.println("---------------------------");
                        });
                    }
                    break;
                case 20:
                    // Consulta: Exercícios por Tipo de Intensidade e Mês
                    List<BasicDBObject> exerciciosPorIntensidadeEMes = exercicioRepo.countExercisesByIntensityAndMonth();
                    if (exerciciosPorIntensidadeEMes.isEmpty()) {
                        System.out.println("Nenhum exercício encontrado.");
                    } else {
                        exerciciosPorIntensidadeEMes.forEach(result -> {
                            String intensidade = result.getString("intensidade");
                            int mes = result.getInt("mes");
                            int ano = result.getInt("ano");
                            int totalExercicios = result.getInt("totalExercicios");
                            System.out.println("Intensidade: " + intensidade);
                            System.out.println("Mês: " + mes + "/" + ano);
                            System.out.println("Total de Exercícios: " + totalExercicios);
                            System.out.println("---------------------------");
                        });
                    }
                    break;

                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Erro ao realizar consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int exibeMenu(Scanner le) {
        System.out.println("#################################");
        System.out.println("### Bem-vindo ao MundoFit ###");
        System.out.println("#################################");
        System.out.println("Escolha uma opção:");
        System.out.println("1) Cadastrar Aluno");
        System.out.println("2) Atualizar Aluno");
        System.out.println("3) Remover Aluno");
        System.out.println("4) Cadastrar Exercício");
        System.out.println("5) Atualizar Exercício");
        System.out.println("6) Remover Exercício");
        System.out.println("7) Realizar Consultas");
        System.out.println("8) Cadastrar Dieta");
        System.out.println("9) Consultar Dietas");
        System.out.println("10) Sair");
        return le.nextInt();
    }
}
