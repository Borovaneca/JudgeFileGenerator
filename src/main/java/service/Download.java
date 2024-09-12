package service;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static constants.URLS.FILE_URL;

public class Download {

    private String directory;
    private String url;
    private String fileId;

    public Download(String directory, String url) {
        this.directory = directory;
        this.url = url;
        this.fileId = "";
    }

    public void download() {
        HttpResponse<String> protectedResponse = Unirest.get(this.url)
                .asString();

        JSONObject responseBody = new JSONObject(protectedResponse.getBody());
        JSONObject contest = responseBody.getJSONObject("contest");

        String contestName = contest.getString("name").replaceAll("[^a-zA-Z0-9]", "");
        File contestDir = new File(this.directory, contestName);
        if (!contestDir.exists()) {
            contestDir.mkdirs();
        }

        JSONArray problems = contest.getJSONArray("problems");
        String fileType = contest.getJSONArray("allowedSubmissionTypes").getJSONObject(0).getString("name").split("\\s+")[0];
         switch (fileType) {
            case "Java":
                 fileType =".java";
                 break;
             case "C#":
                 fileType = ".cs";
                 break;
             case "JavaScript":
                 fileType = ".js";
                 break;
             case "Python":
                 fileType = ".py";
                 break;
             default:
                 fileType = "";
        };
        for (int i = 0; i < problems.length(); i++) {
            JSONObject problem = problems.getJSONObject(i);
            JSONArray resources = problem.getJSONArray("resources");

            String problemName = problem.getString("name").replaceAll("[^a-zA-Z0-9 ]", "");
            String javaFileName = toJavaClassName(problemName);

            generateDefaultFiles(contestDir, javaFileName, fileType, contestName);

            for (int j = 0; j < resources.length(); j++) {
                JSONObject resource = resources.getJSONObject(j);
                if (resource.getString("name").equals("Условия")) {
                    fileId = resource.getString("id");
                }
            }
        }
        getFile(contestDir, fileId);

    }

    private static void generateDefaultFiles(File contestDir, String fileName, String fileType, String contestName) {
        File file = new File(contestDir, fileName + fileType);

        try (FileWriter writer = new FileWriter(file)) {
            switch (fileType.toLowerCase()) {
                case ".java":
                    writer.write("package " + contestName + ";\n\n");
                    writer.write("public class " + fileName + " {\n");
                    writer.write("    public static void main(String[] args) {\n");
                    writer.write("        // TODO: Write your solution here\n");
                    writer.write("    }\n");
                    writer.write("}\n");
                    break;

                case ".py":
                    break;

                case ".cs":
                    writer.write("using System;\n\n");
                    writer.write("namespace " + contestName + " {\n");
                    writer.write("    class " + fileName + " {\n");
                    writer.write("        static void Main(string[] args) {\n");
                    writer.write("            // TODO: Write your solution here\n");
                    writer.write("        }\n");
                    writer.write("    }\n");
                    writer.write("}\n");
                    break;

                case ".js":
                    writer.write("// TODO: Write your solution here\n");
                    writer.write("function main() {\n");
                    writer.write("    // Your code goes here\n");
                    writer.write("}\n\n");
                    writer.write("main();\n");
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported file type: " + fileType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String toJavaClassName(String problemName) {
        problemName = problemName.replaceAll("^\\d+", "");
        problemName = problemName.replaceAll("[^a-zA-Z0-9 ]", "");
        problemName = problemName.trim();

        if (problemName.isEmpty()) {
            return "UnnamedProblem";
        }

        String[] words = problemName.split("\\s+");
        StringBuilder javaClassName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                javaClassName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            }
        }

        return javaClassName.toString();
    }


    private static void getFile (File contestDir, String fileId){
        String url = FILE_URL + fileId;

        String destinationFile = contestDir.getAbsolutePath() + File.separator + "problem_description.docx";

        try {
            HttpResponse<File> accept = Unirest.get(url)
                    .header("Accept", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .asFile(destinationFile);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was a problem downloading the file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
