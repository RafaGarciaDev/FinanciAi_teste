package view;

import controller.FinanciamentoController;
import dao.ClienteDAO;
import dao.ImovelDAO;
import model.entities.Cliente;
import model.entities.Imovel;
import model.enums.TipoAmortizacao;
import model.enums.TipoImovel;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) throws SQLException {
        // Criando instâncias das DAOs
        ClienteDAO clienteDAO = new ClienteDAO();
        ImovelDAO imovelDAO = new ImovelDAO();
        FinanciamentoController financiamentoController = new FinanciamentoController();

        // Adicionando 5 clientes e 5 imóveis ao banco de dados
        adicionarClientes(clienteDAO);
        adicionarImoveis(imovelDAO);

        // Listando clientes e imóveis disponíveis
        listarClientesEImoveis(clienteDAO, imovelDAO);

        // Loop principal do programa
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Nova simulação do zero");
            System.out.println("2 - Consultar simulação existente");
            System.out.println("3 - Sair");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    // Nova simulação do zero
                    novaSimulacao(scanner, clienteDAO, imovelDAO, financiamentoController);
                    break;
                case 2:
                    // Consultar simulação existente
                    consultarSimulacaoExistente(scanner, financiamentoController);
                    break;
                case 3:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 3);

        // Fechando conexões
        clienteDAO.fecharConexao();
        imovelDAO.fecharConexao();
        financiamentoController.fecharConexoes();

        scanner.close();
    }

    // Método para adicionar 5 clientes ao banco de dados
    private static void adicionarClientes(ClienteDAO clienteDAO) throws SQLException {
        Cliente cliente1 = new Cliente(1, "Ana Silva", 3500.00);
        Cliente cliente2 = new Cliente(2, "João Santos", 6000.00);
        Cliente cliente3 = new Cliente(3, "Maria Oliveira", 4200.00);
        Cliente cliente4 = new Cliente(4, "Pedro Costa", 8000.00);
        Cliente cliente5 = new Cliente(5, "Carla Souza", 4800.00);

        clienteDAO.adicionarCliente(cliente1);
        clienteDAO.adicionarCliente(cliente2);
        clienteDAO.adicionarCliente(cliente3);
        clienteDAO.adicionarCliente(cliente4);
        clienteDAO.adicionarCliente(cliente5);

        System.out.println("5 clientes adicionados ao banco de dados.");
    }

    // Método para adicionar 5 imóveis ao banco de dados
    private static void adicionarImoveis(ImovelDAO imovelDAO) throws SQLException {
        Imovel imovel1 = new Imovel(1, 200000.00, TipoImovel.CASA);
        Imovel imovel2 = new Imovel(2, 300000.00, TipoImovel.APARTAMENTO);
        Imovel imovel3 = new Imovel(3, 500000.00, TipoImovel.CASA);
        Imovel imovel4 = new Imovel(4, 400000.00, TipoImovel.APARTAMENTO);
        Imovel imovel5 = new Imovel(5, 600000.00, TipoImovel.CASA);

        imovelDAO.adicionarImovel(imovel1);
        imovelDAO.adicionarImovel(imovel2);
        imovelDAO.adicionarImovel(imovel3);
        imovelDAO.adicionarImovel(imovel4);
        imovelDAO.adicionarImovel(imovel5);

        System.out.println("5 imóveis adicionados ao banco de dados.");
    }

    // Método para nova simulação do zero
    private static void novaSimulacao(Scanner scanner, ClienteDAO clienteDAO, ImovelDAO imovelDAO, FinanciamentoController financiamentoController) throws SQLException {
        // Listar clientes e imóveis disponíveis
        listarClientesEImoveis(clienteDAO, imovelDAO);

        // Solicitar dados do financiamento
        System.out.print("Digite o ID do cliente: ");
        int clienteId = scanner.nextInt();

        // Verificar se o cliente existe
        Cliente cliente = clienteDAO.buscarClientePorId(clienteId);
        if (cliente == null) {
            System.out.println("Cliente não encontrado. Verifique o ID e tente novamente.");
            return;
        }

        System.out.print("Digite o ID do imóvel que deseja financiar: ");
        int imovelId = scanner.nextInt();

        // Buscar o imóvel pelo ID
        Imovel imovel = imovelDAO.buscarImovelPorId(imovelId);
        if (imovel == null) {
            System.out.println("Imóvel não encontrado. Verifique o ID e tente novamente.");
            return;
        }

        // Preencher o valor financiado automaticamente
        double valorFinanciado = imovel.getvalor();
        System.out.println("Valor financiado (automático): " + valorFinanciado);

        System.out.print("Digite a taxa de juros (%): ");
        double taxaJuros = scanner.nextDouble();
        System.out.print("Digite o valor de entrada: ");
        double valorEntrada = scanner.nextDouble();
        System.out.print("Digite o prazo (em meses): ");
        int prazo = scanner.nextInt();

        // Escolha do tipo de amortização usando números
        System.out.print("Digite o tipo de amortização (1 para PRICE, 2 para SAC): ");
        int tipoAmortizacaoNum = scanner.nextInt();
        TipoAmortizacao tipoAmortizacao;

        if (tipoAmortizacaoNum == 1) {
            tipoAmortizacao = TipoAmortizacao.PRICE;
        } else if (tipoAmortizacaoNum == 2) {
            tipoAmortizacao = TipoAmortizacao.SAC;
        } else {
            System.out.println("Opção inválida. Usando PRICE como padrão.");
            tipoAmortizacao = TipoAmortizacao.PRICE;
        }

        // Gerar um ID único para o financiamento
        int financiamentoId = financiamentoController.gerarNovoIdFinanciamento();

        // Simulando o financiamento
        financiamentoController.simularFinanciamento(financiamentoId, clienteId, imovelId, valorFinanciado, taxaJuros, valorEntrada, prazo, tipoAmortizacao);

        // Listando financiamentos e parcelas
        financiamentoController.listarFinanciamentos();
        financiamentoController.listarParcelas(financiamentoId);
    }

    // Método para listar clientes e imóveis disponíveis
    private static void listarClientesEImoveis(ClienteDAO clienteDAO, ImovelDAO imovelDAO) throws SQLException {
        System.out.println("\nClientes cadastrados:");
        List<Cliente> clientes = clienteDAO.listarClientes();
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }

        System.out.println("\nImóveis disponíveis:");
        List<Imovel> imoveis = imovelDAO.listarImoveis();
        for (Imovel imovel : imoveis) {
            System.out.println(imovel);
        }
    }

    // Método para consultar simulação existente
    private static void consultarSimulacaoExistente(Scanner scanner, FinanciamentoController financiamentoController) throws SQLException {
        System.out.println("Financiamentos existentes:");
        financiamentoController.listarFinanciamentos();

        System.out.print("Digite o ID do financiamento que deseja consultar: ");
        int financiamentoId = scanner.nextInt();

        // Listar parcelas do financiamento selecionado
        financiamentoController.listarParcelas(financiamentoId);
    }
}