package br.com.alura.screenmatch.model;

public enum Categoria {
    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime"),
    TERROR("Horror"),
    DOCUMENTARIO("Documentary"),
    FICCAO("Fiction"),
    FANTASIA("Fantasy"),
    ANIMACAO("Animation");

    private String categoriaOMDB;

    Categoria(String categoriaOMDB){
        this.categoriaOMDB = categoriaOMDB;
    }

    public static Categoria fromString(String text){
        for(Categoria categoria : Categoria.values()){
            if(categoria.categoriaOMDB.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: ");
    }
}
