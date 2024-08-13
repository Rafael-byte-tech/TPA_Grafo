import app.Menu;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
    public static void main(String[] args)
    {
        Menu menu;
        String path, graphPath, mstPath;

        /*Caminhos das ENTRADAS e SAÍDAS.*/
        path =      "entrada.txt";          /*Caminho do Grafo (ENTRADA).*/
        graphPath = "grafoCompleto.txt";    /*Caminho do Grafo (SAÍDA).*/
        mstPath =   "agm.txt";              /*Caminho da Árvore Geradora Mínima (SAÍDA).*/

        menu = new Menu(path, graphPath, mstPath);

        menu.callApp();
    }
}