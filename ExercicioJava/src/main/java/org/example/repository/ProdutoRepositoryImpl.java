package org.example.repository;

import org.example.model.Produto;
import org.example.util.ConexaoBanco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositoryImpl implements ProdutoRepository{


    private final static String SELECT =
            """
                SELECT
                    id,
                    nome,
                    preco,
                    quantidade,
                    categoria
                FROM
                    Produto
            """;

    private final static String INSERT =
            """
                INSERT INTO produto
                (
                    nome,
                    preco,
                    quantidade,
                    categoria
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?
                )
                """;


    private final static String UPDATE_BY_ID =
            """
                UPDATE
                    Produto
                SET
                    nome = ?,
                    preco = ?,
                    quantidade = ?,
                    categoria = ?
                WHERE
                    id = ?
                """;

    private final static String DELETE_BY_ID =
            """
                DELETE FROM
                    Produto
                WHERE
                    id = ?
                """;

    @Override
    public Produto save(Produto produto) {

        try(Connection connection = ConexaoBanco.conectar();
            PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){

            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQuantidade());
            statement.setString(4, produto.getCategoria());

            statement.executeUpdate();

            int id = getIdFromDatabase(produto, statement);
            produto.setId(id);

            return produto;

        } catch ( SQLException e){
            throw new RuntimeException("[ERROR] Connection database: " + e.getMessage());
        }

    }

    @Override
    public List<Produto> findAll() {

        try(Connection connection = ConexaoBanco.conectar();
            PreparedStatement statement = connection.prepareStatement(SELECT);
            ResultSet resultSet = statement.executeQuery()
        ){
            List<Produto> produtos = new ArrayList<>();

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String nome = resultSet.getString(2);
                double preco = resultSet.getDouble(3);
                int quantidade = resultSet.getInt(4);
                String categoria = resultSet.getString(5);

                Produto produto = new Produto(id, nome, preco, quantidade, categoria);
                produtos.add(produto);
            }

            return produtos;

        } catch (SQLException e){
            throw new RuntimeException("[ERROR] Connection database: " + e.getMessage());
        }

    }

    @Override
    public Produto findById(int id) {
        String query = SELECT + """
                WHERE
                    id = ?
                """;

        try(Connection connection = ConexaoBanco.conectar();
            PreparedStatement statement = connection.prepareStatement(query);
        ){

            statement.setInt(1, id);

            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()){
                    String nome = resultSet.getString(2);
                    double preco = resultSet.getDouble(3);
                    int quantidade = resultSet.getInt(4);
                    String categoria = resultSet.getString(5);

                    return new Produto(id, nome, preco, quantidade, categoria);
                }

                throw new RuntimeException("[ERROR] User not found in database: ");

            }


        } catch (SQLException e){
            throw new RuntimeException("[ERROR] Connection database: " + e.getMessage());
        }

    }

    @Override
    public Produto update(Produto produto) {

        try(Connection connection = ConexaoBanco.conectar();
            PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID, Statement.RETURN_GENERATED_KEYS)
        ){

            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQuantidade());
            statement.setString(4, produto.getCategoria());
            statement.setInt(5, produto.getId());

            statement.executeUpdate();

            return produto;

        } catch (SQLException e){
            throw new RuntimeException("[ERROR] Connection database: " + e.getMessage());
        }


    }

    @Override
    public void deleteById(int id) {

        try(Connection connection = ConexaoBanco.conectar();
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)
        ){

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("[ERROR] Connection database: " + e.getMessage());
        }

    }

    // Métodos auxiliares

    private int getIdFromDatabase(Produto produto, PreparedStatement statement) throws SQLException {
        try(ResultSet resultSet = statement.getGeneratedKeys()){
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
            throw new RuntimeException("[ERROR] Can not get the id of product ");
        }
    }
}
