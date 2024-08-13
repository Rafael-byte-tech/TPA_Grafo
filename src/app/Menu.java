package app;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu
{
    private App app;
    private final String graphPath, mstPath;

    public Menu(String path, String graphPath, String mstPath)
    {
        try (BufferedReader b = new BufferedReader(new FileReader(path)))
        {
            this.app = new App(b);
        }

        catch (FileNotFoundException exception)
        {
            System.out.print(exception.getMessage());
            this.app = new App();
        }

        catch (IOException exception)
        {
            System.out.println(exception.getMessage());
            this.app = new App();
        }

        this.graphPath = graphPath;
        this.mstPath = mstPath;
    }

    private void addCity(Scanner s)
    {
        String city;

        System.out.print("Cidade:\t");
        city = s.nextLine();

        this.app.addCity(city);
        System.out.print("\nCIDADE ADICIONADA\n");
    }

    private void shortestPath(Scanner s, int option)
    {
        String city1, city2;

        System.out.print("Primeira cidade:\t");
        city1 = s.nextLine();

        System.out.print("Segunda cidade:\t");
        city2 = s.nextLine();

        if (option == 4) this.app.shortestPath(city1, city2);
        else this.app.shortestPathMST(city1, city2);
    }

    private void addRoute(Scanner s) throws InputMismatchException
    {
        String src, destiny;
        float distance;

        System.out.print("Origem:\t");
        src = s.nextLine();

        System.out.print("Destino:\t");
        destiny = s.nextLine();

        System.out.print("Distância:\t");
        distance = s.nextFloat();

        this.app.addRoute(src, destiny, distance);
        System.out.print("\nROTA ADICIONADA.\n");
    }

    private void saveAndClose()
    {
        try
        {
            BufferedWriter b;

            b = new BufferedWriter(new FileWriter(this.graphPath));
            this.app.save(b);

            b.close();

            b = new BufferedWriter(new FileWriter(this.mstPath));
            this.app.saveMST(b);

            b.close();
        }
        catch (IOException exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    private void displayMenu()
    {
        String[] options;

        options = new String[6];

        System.out.print("App do Grafo\n\n");

        options[0] = "1. Acrescentar Cidade\n";
        options[1] = "2. Acrescentar Rota\n";
        options[2] = "3. Calcular árvore geradora mínima (AGM)\n";
        options[3] = "4. Calcular caminho mínimo entre duas cidades\n";
        options[4] = "5. Calcular caminho mínimo entre duas cidades considerando apenas a AGM\n";
        options[5] = "6. Gravar e Sair\n";

        for (String option : options) System.out.print(option);

        System.out.print("\nENTRE O NÚMERO DA OPÇÃO:\t");
    }

    private void clearScreen()
    {
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public void callApp()
    {
        int option;
        Scanner scanner;

        option = -1;
        scanner = new Scanner(System.in);

        while (option != 6)
        {
            try
            {
                this.displayMenu();
                option = scanner.nextInt();
                scanner.nextLine(); /*Consome o \n do nextInt()*/

                switch (option)
                {
                    case 1:
                        System.out.print("\nADICIONAR CIDADE\n");
                        this.addCity(scanner);
                        break;
                    case 2:
                        System.out.print("\nADICIONAR ROTA\n");
                        this.addRoute(scanner);
                        break;
                    case 3:
                        System.out.print("\nCALCULAR AGM\n");
                        this.app.displayMST();
                        break;
                    case 4:
                        System.out.print("\nCALCULAR MENOR CAMIHO\n");
                        this.shortestPath(scanner, 4);
                        break;
                    case 5:
                        System.out.print("\nCALCULAR MENOR CAMINHO (AGM)\n");
                        this.shortestPath(scanner, 5);
                        break;
                    case 6:
                        this.saveAndClose();
                        System.out.print("\nFIM.\n");
                        System.exit(0);
                }
            }

            catch (InputMismatchException exception)
            {
                System.out.println(exception.getMessage());
            }

            System.out.println("ENTRE QUALQUER TECLA PARA CONTINUAR...");
            String pause = scanner.nextLine();
            this.clearScreen();
        }
    }
}