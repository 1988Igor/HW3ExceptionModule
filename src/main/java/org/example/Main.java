package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean allConditionsMet = false;
        while (!allConditionsMet) {
            System.out.println("Введите данные в формате: Фамилия Имя Отчество, дата рождения(строка формата dd.mm.yyyy)" +
                    " номер телефона, пол");
            String userInput = scanner.nextLine();

            try {
                processUserData(userInput);
                allConditionsMet = true;
                System.out.println("Данные успешно обработаны и сохранены.");
            } catch (UserDataValidationException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Ошибка при записи в файл:");
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    private static void processUserData(String userInput) throws UserDataValidationException, IOException {
        String[] data = userInput.split(" ");
        if (data.length != 6) {
            throw new UserDataValidationException("Введено неверное количество данных.");
        }

        String lastName = data[0];
        String firstName = data[1];
        String patronymic = data[2];
        String dateOfBirth = data[3];
        String phoneNumber = data[4];
        String gender = data[5];

        validateData(lastName, firstName, patronymic, dateOfBirth, phoneNumber, gender);

        String filename = lastName + ".txt";
        String fileContent = lastName + " " + firstName + " " + patronymic +
                             " " + dateOfBirth + " " + phoneNumber + " " + gender;

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(fileContent);
        }
    }

    private static void validateData(String lastName, String firstName, String middleName, String dateOfBirth,
                                     String phoneNumber, String gender) throws UserDataValidationException {
        if (!isValidDate(dateOfBirth)) {
            throw new UserDataValidationException("Неверный формат даты рождения, введите только цифры: " + dateOfBirth);
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            throw new UserDataValidationException("Неверный формат, номер телефона должен быть целым числом без знака: " + phoneNumber);
        }

        if (!isValidGender(gender)) {
            throw new UserDataValidationException("Неверный формат, пол должен быть представлен символом 'f' или 'm': " + gender);
        }
    }

    private static boolean isValidDate(String date) {
        String regex = "^\\d{2}\\.\\d{2}\\.\\d{4}$";
        return date.matches(regex);
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        try {
            Long.parseLong(phoneNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidGender(String gender) {
        return gender.equals("f") || gender.equals("m");
    }
}
