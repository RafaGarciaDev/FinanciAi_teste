package view;

import model.controller.FinanciamentoController;
import dao.ClienteDAO;
import dao.ImovelDAO;
import model.entities.Cliente;
import model.entities.Imovel;
import model.enums.TipoAmortizacao;
import model.enums.TipoImovel;

import java.sql.SQLException;

public class Program {
    public static void main(String[] args) throws SQLException {
        // Criando instâncias das DAOs
        ClienteDAO clienteDAO = new ClienteDAO();
        ImovelDAO imovelDAO = new ImovelDAO();

        // Adicionando clientes
        Cliente cliente1 = new Cliente(1, "Ana Silva", 3500.00);
        clienteDAO.adicionarCliente(cliente1);

        // Adicionando imóveis
        Imovel imovel1 = new Imovel(1, 500000.00, TipoImovel.APARTAMENTO);
        imovelDAO.adicionarImovel(imovel1);

        // Criando o controlador de financiamentos
        FinanciamentoController financiamentoController = new FinanciamentoController();

        // Simulando um financiamento
        financiamentoController.simularFinanciamento(1, 1, 1, 400000.00, 13.0, 100000.00, 130, TipoAmortizacao.SAC);

        // Listando financiamentos e parcelas
        financiamentoController.listarFinanciamentos();
        financiamentoController.listarParcelas(1);

        // Fechando conexões
        clienteDAO.fecharConexao();
        imovelDAO.fecharConexao();
        financiamentoController.fecharConexoes();
    }
}