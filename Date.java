package Date;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Date {
	public static String createFileName() {
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();
		String formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
		String name = formattedDate + now.getHour();
		name = name.replace(".", "");
		return name;
	}

	public static boolean createFile(String fileName) throws IOException {
		try {
			File file = new File(fileName + ".txt");
			if (file.createNewFile()) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return false;
		}
	}

	public static void writeIntoFile(String fileName, String messages) throws IOException {
		boolean checkingForAppendMode = createFile(fileName);
		try {
			FileWriter myWriter = new FileWriter(fileName + ".txt", true);
			if (checkingForAppendMode) {
				myWriter.write(messages);
			} else {
				// myWriter = new FileWriter(fileName + ".txt",true);
				Files.write(Paths.get(fileName + ".txt"), messages.getBytes(), StandardOpenOption.APPEND);
			}

			myWriter.close();
			// System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}
