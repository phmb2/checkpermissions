package com.phmb2.checkpermission.models;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by phmb2 on 14/08/17.
 */

public class GrupoPermissoes
{
    public String nomeGrupoPermissoes;
    public String descricaoGrupoPermissoes;
    public int quantidadeApps;
    public Set<String> pacotesApp = new HashSet<>();
}
