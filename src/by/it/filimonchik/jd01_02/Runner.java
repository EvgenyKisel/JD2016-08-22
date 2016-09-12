package by.it.filimonchik.jd01_02;

import java.util.Scanner;

/**
 * Created by Raman.Filimonchyk on 04.09.2016.
 */
public class Runner {

    public static void main(String[] args) {

        String line = Util.getLineFromConsole();
        System.out.println("������ ����� = " + line);
        int mas[] = Util.getIntFromLine(line);
        String arrStr[] = line.split(" ");

        System.out.println("������ �1");
        String maxStr = TaskA.maxLength(arrStr);
        System.out.println("����� ������� ����� = " + maxStr);
        System.out.println("����� ����� ����� = " + maxStr.length());

        String minStr = TaskA.minLength(arrStr);
        System.out.println("����� �������� ����� = " + minStr);
        System.out.println("����� ����� ����� = " + minStr.length());

        System.out.println("������ �2");
        double mean = TaskA.mean(mas);
        System.out.println("������� ��������������: " + mean);
        for (int i : mas) {
            if (i < mean) {
                System.out.print(i + " ");
            }
        }
        System.out.println("\n������ �3");
        for (String i : arrStr) {
            if (TaskA.uniqueDigits(i)) {
                System.out.print("����� �� ������ �����: " + i);
                break;
            }
        }
    }
}