/*
Постараюсь максимально комментариями пояснить, что я делаю в этой лабораторной
 */

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ComplexMatrix A = null;
        ComplexMatrix B = null;

        while (true) {
            printMenu(A, B);
            int choice = readInt("Выберите пункт: ");

            try {
                switch (choice) {
                    case 1:
                        A = createMatrix("A");
                        break;
                    case 2:
                        B = createMatrix("B");
                        break;
                    case 3:
                        require(A, "A");
                        require(B, "B");
                        System.out.println("A + B =");
                        System.out.println(A.add(B));
                        break;
                    case 4:
                        require(A, "A");
                        require(B, "B");
                        System.out.println("A * B =");
                        System.out.println(A.multiply(B));
                        break;
                    case 5:
                        require(A, "A");
                        require(B, "B");
                        System.out.println("A / B = A * B^(-1) =");
                        System.out.println(A.divide(B));
                        break;
                    case 6:
                        require(A, "A");
                        System.out.println("Aᵀ =");
                        System.out.println(A.transpose());
                        break;
                    case 7:
                        require(A, "A");
                        System.out.println("det(A) = " + A.determinant());
                        break;
                    case 8:
                        require(A, "A");
                        System.out.println("A^(-1) =");
                        System.out.println(A.inverse());
                        break;
                    case 9:
                        require(A, "A");
                        System.out.println("A =");
                        System.out.println(A);
                        break;
                    case 10:
                        require(B, "B");
                        System.out.println("B =");
                        System.out.println(B);
                        break;
                    case 0:
                        System.out.println("Выход.");
                        return;
                    default:
                        System.out.println("Нет такого пункта.");
                }
            } catch (IllegalArgumentException | IllegalStateException | ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu(ComplexMatrix A, ComplexMatrix B) {
        System.out.println("========= МЕНЮ =========");
        System.out.println("A: " + describe(A));
        System.out.println("B: " + describe(B));
        System.out.println("------------------------");
        System.out.println("1. Создать матрицу A");
        System.out.println("2. Создать матрицу B");
        System.out.println("3. A + B");
        System.out.println("4. A * B");
        System.out.println("5. A / B  (A * B^(-1))");
        System.out.println("6. Aᵀ (транспонирование A)");
        System.out.println("7. det(A)");
        System.out.println("8. A^(-1)");
        System.out.println("9. Показать A");
        System.out.println("10. Показать B");
        System.out.println("0. Выход");
        System.out.println("========================");
    }

    private static String describe(ComplexMatrix m) {
        if (m == null) return "(не задана)";
        return m.getRows() + "x" + m.getCols();
    }

    private static void require(ComplexMatrix m, String name) {
        if (m == null) {
            throw new IllegalStateException("Матрица " + name + " не задана");
        }
    }

    private static ComplexMatrix createMatrix(String name) {
        System.out.println("Создание матрицы " + name);
        int rows = readPositiveInt("  Число строк: ");
        int cols = readPositiveInt("  Число столбцов: ");

        Complex[][] data = new Complex[rows][cols];
        System.out.println("Введите элементы. Для каждого — два числа: Re и Im.");
        System.out.println("Пример: \"1 0\" — это 1, \"0 -2\" — это -2i.");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("  %s[%d][%d] Re: ", name, i, j);
                double re = readDouble("");
                System.out.printf("  %s[%d][%d] Im: ", name, i, j);
                double im = readDouble("");
                data[i][j] = new Complex(re, im);
            }
        }

        ComplexMatrix m = new ComplexMatrix(data);
        System.out.println("Матрица " + name + " создана:");
        System.out.println(m);
        return m;
    }

    //Ниже функции-проверки

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Нужно целое число. Попробуйте снова.");
            }
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v > 0) return v;
            System.out.println("Число должно быть больше 0.");
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            if (!prompt.isEmpty()) System.out.print(prompt);
            String line = sc.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Нужно число. Попробуйте снова.");
            }
        }
    }
}