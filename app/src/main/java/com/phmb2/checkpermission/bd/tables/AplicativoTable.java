package com.phmb2.checkpermission.bd.tables;

import com.phmb2.checkpermission.bd.DBColumn;
import com.phmb2.checkpermission.bd.DBTable;

/**
 * Created by phmb2 on 11/09/17.
 */

public class AplicativoTable extends DBTable
{
    public static final String TABLE_NAME = "APLICATIVO";

    //Columns Index
    public static final int ID_COL_INDEX = 0;
    public static final int NOME_COL_INDEX = 1;
    public static final int ICONE_COL_INDEX = 2;
    public static final int NOME_PACOTE_COL_INDEX = 3;
    public static final int VERSAO_COL_INDEX = 4;
    public static final int DATA_ATUALIZACAO_COL_INDEX = 5;

    //Columns Names
    public static final String ID_COL = "Id";
    public static final String NOME_COL = "Nome";
    public static final String ICONE_COL = "Icone";
    public static final String NOME_PACOTE_COL = "Nome_Pacote";
    public static final String VERSAO_COL = "Versao";
    public static final String DATA_ATUALIZACAO_COL = "Data_Atualizacao";

    private static final DBColumn[] DB_COLUMNS = new DBColumn[] {
            new DBColumn(ID_COL, DBColumn.TYPE_INTEGER).setPrimaryKey(),
            new DBColumn(NOME_COL, DBColumn.TYPE_TEXT),
            new DBColumn(ICONE_COL, DBColumn.TYPE_TEXT),
            new DBColumn(NOME_PACOTE_COL, DBColumn.TYPE_INTEGER),
            new DBColumn(VERSAO_COL, DBColumn.TYPE_TEXT),
            new DBColumn(DATA_ATUALIZACAO_COL, DBColumn.TYPE_TEXT)
    };

    public AplicativoTable() {
        super(TABLE_NAME, DB_COLUMNS);
    }
}
