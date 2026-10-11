public class ComplexMatrix {
    private final int rows;
    private final int cols;
    private final Complex[][] data;

    // Проверка, что комплексное число равно 0 (может быть мусор после вычислений, поэтому проверяем)

    public boolean isNull(Complex B){
        if( Math.abs(B.getRe()) < 1e-9 && Math.abs(B.getIm()) < 1e-9 ){
            return true;
        }
        return false;
    }

    //Кастомный компаратор, который сравнивает 2 комплексных числа (в моей реализации мы используем модуль комплексного числа)

    public boolean greater(Complex A, Complex B){
        return A.getRe()*A.getRe() + A.getIm()*A.getIm() > B.getRe()*B.getRe() + B.getIm()*B.getIm();
    }

    //Конструктор матрицы по её размерам

    public ComplexMatrix(int rows, int cols){
        if(rows <= 0 || cols <= 0){
            throw new IllegalArgumentException("Размеры матрицы должны быть больше 0");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new Complex[rows][cols];
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < cols; ++j){
                this.data[i][j] = new Complex(0, 0);
            }
        }
    }

    //Конструктор матрицы по двумерному массиву комплексных чисел

    public ComplexMatrix(Complex[][] data){
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }

    //геттеры и сеттеры

    public Complex getVal(int r, int c){
        return data[r][c];
    }
    public void set(int r, int c, Complex val){
        data[r][c] = val;
    }


    //Сложение двух матриц

    public ComplexMatrix add(ComplexMatrix B){
        if (this.rows != B.rows || this.cols != B.cols) {
            throw new IllegalArgumentException("Для сложения размеры матриц должны совпадать");
        }
        ComplexMatrix result = new ComplexMatrix(rows, cols);
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < cols; ++j){
                result.set(i,j, this.data[i][j].sum(B.getVal(i,j)));
            }
        }
        return result;
    }

    //Умножение двух матриц

    public ComplexMatrix multiply(ComplexMatrix B) {
        if(this.cols != B.rows){
            throw new IllegalArgumentException("Количество столбцов первой матрицы должно совпадать с числом строк второй");
        }
        ComplexMatrix result = new ComplexMatrix(this.rows, B.cols);
        for(int i = 0; i < this.rows; ++i){
            for(int j = 0; j < B.cols; ++j){
                Complex val = new Complex(0);
                for(int k = 0; k < this.cols; ++k){
                    val = val.sum(this.data[i][k].multiply(B.data[k][j]));
                }
                result.set(i, j, val);
            }
        }
        return result;
    }

    //Транспозиция матрицы

    public ComplexMatrix transpose() {
        ComplexMatrix result = new ComplexMatrix(cols, rows);
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < cols; ++j){
                result.set(j, i, this.data[i][j]);
            }
        }
        return result;
    }

    //Вычисление определителя методом Гаусса

    public Complex determinant() {
        if (rows != cols) {
            throw new IllegalStateException("Определитель можно вычислить только для квадратной матрицы");
        }
        Complex[][] result = new Complex[rows][cols];

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                result[i][j] = data[i][j];
            }
        }
        Complex det = new Complex(1);

        for (int i = 0; i < rows; ++i) {

            // Выбираем максимальный ведущий элемент
            int k = i;
            for (int j=i+1; j<rows; ++j)
                if (greater(result[j][i], result[k][i]))
                    k = j;

            //Если не нашлось ненулевого, то определитель 0

            if (isNull(result[k][i]))
                return new Complex(0);

            //Иначе свапаем строки
            if (i != k) {
                Complex[] temp = result[i];
                result[i] = result[k];
                result[k] = temp;

                det = det.multiply(new Complex(-1));
            }


            //После чего выполняем выравнивание строк
            for (int j = i + 1; j < rows; ++j) {
                Complex d = result[j][i].divide(result[i][i]);
                result[j][i] = new Complex(0);
                for (int p = i + 1; p < cols; ++p) {
                    Complex newVal = result[j][p].subtract(result[i][p].multiply(d));
                    result[j][p] = newVal;
                }
            }

        }
        //Вычисляем определитель по диагональным элементам верхнетреугольной матрицы
        for(int i = 0; i < rows; ++i)
            det = det.multiply(result[i][i]);
        return det;
    }

    //Находим обратную матрицу

    public ComplexMatrix inverse(){
        if (rows != cols) {
            throw new IllegalStateException("Обратную матрицу можно найти только для квадратной матрицы");
        }

        int n = rows;

        //Расширенная матрица (A | E)
        Complex[][] extended = new Complex[n][2 * n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                extended[i][j] = data[i][j];
            }
            for (int j = 0; j < n; j++) {
                extended[i][n + j] = new Complex(i == j ? 1.0 : 0.0);
            }
        }

        //Прямой ход метода Гаусса
        for (int col = 0; col < n; col++) {

            //Ищем ведущий элемент
            int pivotRow = -1;
            for (int r = col; r < n; r++) {
                if (!isNull(extended[r][col])) {
                    if (pivotRow == -1 || greater(extended[r][col], extended[pivotRow][col])) {
                        pivotRow = r;
                    }
                }
            }

            if (pivotRow == -1) {
                throw new ArithmeticException("Матрица вырождена: обратная матрица не существует");
            }

            //Свапаем строки
            if (pivotRow != col) {
                Complex[] temp = extended[col];
                extended[col] = extended[pivotRow];
                extended[pivotRow] = temp;
            }

            //Нормируем ведущую строку
            Complex pivot = extended[col][col];
            for (int j = 0; j < 2 * n; j++) {
                extended[col][j] = extended[col][j].divide(pivot);
            }

            // Обнуляем текущий столбец во всех остальных строках
            for (int r = 0; r < n; r++) {
                if (r == col) continue;

                Complex factor = extended[r][col];
                if (isNull(factor)) continue;

                for (int j = 0; j < 2 * n; j++) {
                    extended[r][j] = extended[r][j].subtract(factor.multiply(extended[col][j]));
                }

                //Для устранения погрешности
                extended[r][col] = new Complex(0.0);
            }
        }

        //Правая половина расширенной матрицы — это обратная матрица
        Complex[][] inv = new Complex[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv[i][j] = extended[i][n + j];
            }
        }

        return new ComplexMatrix(inv);
    }

    //Делим матрицу А на B
    public ComplexMatrix divide(ComplexMatrix B) {
        if (this.cols != B.rows) {
            throw new IllegalArgumentException(
                    "Число столбцов первой матрицы должно совпадать с числом строк второй");
        }
        // A / B = A * B^(-1)
        ComplexMatrix invB = B.inverse();
        return this.multiply(invB);
    }


    //Переопределяем вывод, чтобы выводилась матрица
    @Override
    public String toString() {
        // Сначала найдём максимальную ширину строкового представления элемента
        String[][] str = new String[rows][cols];
        int width = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                str[i][j] = String.valueOf(data[i][j]);
                if (str[i][j].length() > width) width = str[i][j].length();
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("| ");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%" + width + "s", str[i][j]));
                if (j < cols - 1) sb.append("  ");
            }
            sb.append(" |");
            if (i < rows - 1) sb.append("\n");
        }
        return sb.toString();
    }

}