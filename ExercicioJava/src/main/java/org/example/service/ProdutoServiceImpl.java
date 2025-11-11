package org.example.service;

import org.example.model.Produto;
import org.example.repository.ProdutoRepository;

import java.util.List;

public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto cadastrarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    @Override
    public Produto buscarPorId(int id) {
        return produtoRepository.findById(id);
    }

    @Override
    public Produto atualizarProduto(Produto produto, int id) {
        produto.setId(id);
        return produtoRepository.update(produto);
    }

    @Override
    public boolean excluirProduto(int id) {
        return produtoRepository.deleteById(id);
    }
}
