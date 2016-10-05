package by.it.grechishnikov.matLab.controller;


import by.it.grechishnikov.matLab.controller.operation.*;
import by.it.grechishnikov.matLab.model.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private IReadable reader;
    private Operation operation;
    private String name = "";

    public Parser(IReadable reader) {
        this.reader = reader;
        this.operation = new Operation();
    }

    /**
     * Запускаем парсер
     * @param text - строка для парсинга
     * @return - возвращаем распарсенное уравнение в виде строки
     */
    public String run(String text) {
        Var var = parse(text);
        var.assign();
        name = "";
        return var.toString();
    }

    /**
     * Одному богу известно, что тут происходит
     * @param text - уравнение
     * @return - чудесным образом полученная переменная
     */
    private Var parse(String text) {
        if (text.isEmpty()) {
            System.out.println("Ошибка. Пустая строка.");
            return null;
        } //TODO пофиксить метод, что бы тесты проходили и добавить поддержку векторов
        name = getName(text);
        //получаем уравнение
        String equation = getEquation(text);
        //раскрываем скобки
        while(equation.contains("(")) {
            Pattern pattern = Pattern.compile("[(]{1}[0-9. +*/a-zA-Z-]+[)]{1}");
            Matcher matcher = pattern.matcher(equation);
            matcher.find();
            String inner = matcher.group();
            String tmp = inner.replaceAll("[()]", "");
            Var var = parse(tmp);
            equation = equation.replace(inner, var.valueToString());
        }
        //получаем очередь чисел и переменных
        List<Var> list = getVars(equation, name);
        //получаем операторы
        String[] operators = getOperators(equation);
        Var first = list.get(0);
        list.remove(first);
        int count = 0;

        //делаем умножение и деление
        Iterator<Var> iterator = list.iterator();
        while(iterator.hasNext()) {
            String s = operators[count];
            if(s == null) break;
            if (s.equals("*")) {
                Var second = iterator.next();
                list.remove(second);
                first = operation.mul(name, first, second);
            } else if (s.equals("/")) {
                Var second = iterator.next();
                list.remove(second);
                first = operation.div(name, first, second);
            }
            count++;
        }

        //делаем сложение и вычитание
        iterator = list.iterator();
        for(int i=0; i<operators.length; i++) {
            String s = operators[i];
            if(s == null) break;
            if(s.equals("+")) {
                first = operation.add(name, first, iterator.next());
            } else if(s.equals("-")) {
                first = operation.sub(name, first, iterator.next());
            }
        }
        return first;
    }

    private String getName(String text) {
        if(name.isEmpty()) {
            name = text.substring(0, text.indexOf("="));
        }
        return name;
    }

    /**
     * Возвращает правую часть уравнения
     * @param text - все уравнение
     * @return - правая часть уравнения
     */
    private String getEquation(String text) {
        if(!text.contains("=")) {
            return text;
        } else {
            String equation = text.substring(text.indexOf("=") + 1);
            equation = equation.trim();
            return equation;
        }
    }

    /**
     * Возвращает список переменных и чисел
     * @param equation - уравнение
     * @param name - имя для будущей переменной
     * @return - список чисел и переменных
     */
    private List<Var> getVars(String equation, String name) {
        String[] arg = equation.split("[-+*/ ]+");
        List<Var> queue = new CopyOnWriteArrayList<>();
        for (String s : arg) {
            try {
                double d = Double.parseDouble(s);
                Scalar scalar = new Scalar(name, d);
                queue.add(scalar);
            } catch (NumberFormatException e) {
                try {
                    queue.add(Runner.storage.get(s));
                } catch (NullPointerException e2) {
                    System.out.println("Ошибка. Нет такого объекта в памяти.");
                    return null;
                }
            }
        }
        return queue;
    }

    /**
     * Получаем массив операторов
     * @param equation - уравнение
     * @return - массив операторов
     */
    private String[] getOperators(String equation) {
        String[] operators = new String[100];
        Pattern pattern = Pattern.compile("[-+*/]+");
        Matcher matcher = pattern.matcher(equation);
        for (int i = 0; i < operators.length; i++) {
            if (matcher.find()) {
                operators[i] = matcher.group();
            } else break;
        }
        return operators;
    }
}
