package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String []args) {
        System.out.println("hello");

        try {
            // poczytac o tym co to jest classloader
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connetion = DriverManager
                            .getConnection("jdbc:mysql://localhost:3306/fishproject", "root", "dupa");
            Statement statement = connetion.createStatement();
            // fish
            Long a = 3L;
            String b = String.valueOf(a);
            int firstValue = Integer.valueOf(b);
            statement.execute("insert into fish(id, name, type, price) values (" + firstValue + ",'fish2', 'fish3', 100)");
            ResultSet resultSet = statement.executeQuery("select * from fish");

            while (resultSet.next()) {
                System.out.println(
                        resultSet.getInt(1) + " " +
                        resultSet.getString(2) + " " +
                        resultSet.getString(3) + " " +
                        resultSet.getInt(4));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
-> Mamy dwie encje: Ryba i Akwarium
-> Każda ryba może być w jednym akwarium oraz akwarium może mieć wiele ryb

1. Możliwość stworzenia akwarium/ryby
    a) akwrium (id, nazwa, pojemnosc)
    b) ryba (id, nazwa, gatunek, cena)

2. Nie moze istniec ryba bez przypisanego akwarium
3. Mozliwosc wypisania ryb w konkretnym akwarium
4. Usuwanie akwarium, ale tylko jesli jest puste
5. Nie mozna usuwac ryb, mozna jedynie je przenosic miedzy akwariami
6. Na kazdy 1l akwarium moze byc 1 ryba
 */