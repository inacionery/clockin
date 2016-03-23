/**
 *
 */

package decryptMDB;

import static decryptMDB.ClockRecord.RECORD_FORMAT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class WorkTimeFileReader {

	public static void main(String[] args) throws IOException {

		WorkTimeFileReader workTimeFileReader = new WorkTimeFileReader();

		workTimeFileReader.readStreamOfLinesUsingFilesWithTryBlock();
	}

	public void readStreamOfLinesUsingFilesWithTryBlock()
		throws IOException {

		String path = "/Users/miguelangelo/Liferay/ide/mars/eclipse/workspace_gs_queiroz_galvao/decryptMDB/temp.txt";

		List<ClockRecord> clockRecords = null;

		try (Stream<String> stream = Files.lines(Paths.get(path))) {
			clockRecords = stream
		        .filter(line -> line.matches(RECORD_FORMAT))
		        .map(line -> new ClockRecord(line))
		        .collect(Collectors.toList());
		}

		for (ClockRecord clockRecord : clockRecords) {
			System.out.println(clockRecord.getOriginalRecord());
		}
	}

}
