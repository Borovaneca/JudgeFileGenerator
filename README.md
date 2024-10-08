# Judge File Generator

## Описание

**Judge File Generator** е приложение, създадено за автоматично изтегляне и генериране на файлове за програмни задачи от Judge системата на SoftUni. Приложението позволява на потребителите лесно да изтеглят задачите от дадена лекция и автоматично да създават структура от файлове с начални шаблони за различни програмни езици.

## Инсталация и стартиране

### 1. Сваляне на репозиторито

За да свалите репозиторито локално, изпълнете следната команда в терминала:

```bash
git clone https://github.com/Borovaneca/JudgeFileGenerator.git
```

## 2. Стартиране на приложението чрез `FileGenerator.exe`

След като репозиторито е свалено, можете да стартирате приложението директно чрез изпълнимия файл `FileGenerator.exe`, който се намира в папката с проекта.

## 3. Логване в SoftUni

При стартиране на приложението първо ще трябва да въведете вашето потребителско име и парола за SoftUni. Това е необходимо, за да се свърже приложението с вашия профил в системата Judge.
<div align="center">
  <img src="https://github.com/Borovaneca/JudgeFileGenerator/blob/master/images/login.png" alt="Логване в Judge системата" width="200"/>
</div>

## 4. Въвеждане на URL към задачите в Judge

След успешен логин ще трябва да въведете URL към задачите от съответната лекция в Judge системата. Пример за такъв URL е:
```bash
https://alpha.judge.softuni.org/contests/for-loop-exercise/2394/compete
```
Този линк ще изтегли задачите от лекцията Java For Loop - Exercise.
<div align="center">
  <img src="https://github.com/Borovaneca/JudgeFileGenerator/blob/master/images/link.png" alt="Линка към лекцията в Judge" width="200"/>
</div>

## 5. Избор на път за записване на файловете

След въвеждането на URL, приложението ще поиска да изберете директорията, където да създаде папката с файловете. Папката ще бъде наименувана според името на лекцията. Например, ако лекцията е First Steps In Coding - Lab, името на папката ще бъде `FirstStepsInCodingLab`.

<div align="center">
  <img src="https://github.com/Borovaneca/JudgeFileGenerator/blob/master/images/select_dir.png" alt="Избиране на директория" width="200"/> <img src="https://github.com/Borovaneca/JudgeFileGenerator/blob/master/images/saved_dir.png" alt="Избраната директория" width="200"/>
</div>

## 6. Генериране на файлове и шаблони

За всяка задача от лекцията, приложението ще създаде файл, базиран на програмния език, за който е предназначена задачата. За всяка задача се генерира и файл с началния код (шаблон), който може да бъде различен в зависимост от езика на задачата.
<div align="center">
  <img src="https://github.com/Borovaneca/JudgeFileGenerator/blob/master/images/saved_files.png" alt="Линка към лекцията в Judge" width="200"/>
</div>

Примерен шаблон за Java:
```java
package currentPackage;

public class ClassName {
    public static void main(String[] args) {
        // TODO: Write your solution here
    }
}
```

## 7. Успешно приключване

След като въведете коректно потребителско име, парола и URL, и изберете пътя за записване на файловете, приложението ще извърши генерирането на файловете. Ако всичко е наред, на екрана ще се появи съобщение за успешна операция и приложението ще се затвори автоматично.
<div align="center">
  <img src="https://github.com/Borovaneca/JudgeFileGenerator/blob/master/images/completed.png" alt="Линка към лекцията в Judge" width="200"/>
</div>

# Стартиране чрез IntelliJ IDEA

Можете също така да стартирате приложението директно от средата за разработка IntelliJ IDEA. За да направите това, трябва да изпълните Main класа в проекта.
Стъпки:
- Отворете проекта в IntelliJ IDEA.
- Навигирайте до класа Main.
- Стартирайте класа Main, като използвате IntelliJ IDEA (десен бутон на мишката върху класа -> Run Main).
Това ще стартира приложението в средата за разработка.

# Заключение

`Judge File Generator` е полезен инструмент за всеки, който иска да автоматизира процеса на изтегляне и създаване на файлове за задачите от Judge системата на SoftUni. Той генерира необходимите файлове и начални шаблони, които ви позволяват бързо да започнете работа по всяка задача. Следвайте стъпките в този документ, за да използвате приложението ефективно.
