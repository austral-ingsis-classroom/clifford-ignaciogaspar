package edu.austral.ingsis;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.austral.ingsis.clifford.CommandParser;
import edu.austral.ingsis.clifford.CommandRegistry;
import edu.austral.ingsis.clifford.FileSystemRunner;
import edu.austral.ingsis.clifford.factories.CdFactory;
import edu.austral.ingsis.clifford.factories.LsFactory;
import edu.austral.ingsis.clifford.factories.MkdirFactory;
import edu.austral.ingsis.clifford.factories.PwdFactory;
import edu.austral.ingsis.clifford.factories.RmFactory;
import edu.austral.ingsis.clifford.factories.TouchFactory;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FileSystemTests {
  private final CommandRegistry registry =
      new CommandRegistry(
          Map.of(
              "ls",
              new LsFactory(),
              "cd",
              new CdFactory(),
              "pwd",
              new PwdFactory(),
              "rm",
              new RmFactory(),
              "touch",
              new TouchFactory(),
              "mkdir",
              new MkdirFactory()));
  private final CommandParser parser = new CommandParser(registry);
  private final FileSystemRunner runner = new FileSystemRunner(parser);

  private void executeTest(List<Map.Entry<String, String>> commandsAndResults) {
    final List<String> commands = commandsAndResults.stream().map(Map.Entry::getKey).toList();
    final List<String> expectedResult =
        commandsAndResults.stream().map(Map.Entry::getValue).toList();

    final List<String> actualResult = runner.executeCommands(commands);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void test1() {
    executeTest(
        List.of(
            entry("ls", ""),
            entry("mkdir horace", "'horace' directory created"),
            entry("ls", "horace"),
            entry("mkdir emily", "'emily' directory created"),
            entry("ls", "horace emily"),
            entry("ls --ord=asc", "emily horace")));
  }

  @Test
  void test2() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("mkdir emily", "'emily' directory created"),
            entry("mkdir jetta", "'jetta' directory created"),
            entry("ls", "horace emily jetta"),
            entry("cd emily", "moved to directory 'emily'"),
            entry("pwd", "/emily"),
            entry("touch elizabeth.txt", "'elizabeth.txt' file created"),
            entry("mkdir t-bone", "'t-bone' directory created"),
            entry("ls", "elizabeth.txt t-bone")));
  }

  @Test
  void test3() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("mkdir emily", "'emily' directory created"),
            entry("mkdir jetta", "'jetta' directory created"),
            entry("cd emily", "moved to directory 'emily'"),
            entry("touch elizabeth.txt", "'elizabeth.txt' file created"),
            entry("mkdir t-bone", "'t-bone' directory created"),
            entry("ls", "elizabeth.txt t-bone"),
            entry("rm t-bone", "cannot remove 't-bone', is a directory"),
            entry("rm --recursive t-bone", "'t-bone' removed"),
            entry("ls", "elizabeth.txt"),
            entry("rm elizabeth.txt", "'elizabeth.txt' removed"),
            entry("ls", "")));
  }

  @Test
  void test4() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("mkdir emily", "'emily' directory created"),
            entry("cd horace", "moved to directory 'horace'"),
            entry("mkdir jetta", "'jetta' directory created"),
            entry("cd ..", "moved to directory '/'"),
            entry("cd horace/jetta", "moved to directory 'jetta'"),
            entry("pwd", "/horace/jetta"),
            entry("cd /", "moved to directory '/'")));
  }

  @Test
  void test5() {
    executeTest(
        List.of(
            entry("mkdir emily", "'emily' directory created"),
            entry("cd horace", "'horace' directory does not exist")));
  }

  @Test
  void test6() {
    executeTest(List.of(entry("cd ..", "moved to directory '/'")));
  }

  @Test
  void test7() {
    executeTest(
        List.of(
            entry("mkdir horace", "'horace' directory created"),
            entry("cd horace", "moved to directory 'horace'"),
            entry("touch emily.txt", "'emily.txt' file created"),
            entry("touch jetta.txt", "'jetta.txt' file created"),
            entry("ls", "emily.txt jetta.txt"),
            entry("rm emily.txt", "'emily.txt' removed"),
            entry("ls", "jetta.txt")));
  }

  @Test
  void test8() {
    executeTest(
        List.of(
            entry("mkdir emily", "'emily' directory created"),
            entry("cd emily", "moved to directory 'emily'"),
            entry("mkdir emily", "'emily' directory created"),
            entry("touch emily.txt", "'emily.txt' file created"),
            entry("touch jetta.txt", "'jetta.txt' file created"),
            entry("ls", "emily emily.txt jetta.txt"),
            entry("rm --recursive emily", "'emily' removed"),
            entry("ls", "emily.txt jetta.txt"),
            entry("ls --ord=desc", "jetta.txt emily.txt")));
  }

  @Test
  void test9_nestedDirectories() {
    executeTest(
        List.of(
            entry("mkdir -p a/b/c", "invalid directory name: -p a/b/c"),
            entry("mkdir a", "'a' directory created"),
            entry("cd a", "moved to directory 'a'"),
            entry("mkdir b", "'b' directory created"),
            entry("cd b", "moved to directory 'b'"),
            entry("mkdir c", "'c' directory created"),
            entry("ls", "c"),
            entry("pwd", "/a/b"),
            entry("cd ..", "moved to directory 'a'"),
            entry("ls", "b")));
  }

  @Test
  void test10_touchMultipleFiles() {
    executeTest(
        List.of(
            entry("touch file1.txt", "'file1.txt' file created"),
            entry("touch file2.txt", "'file2.txt' file created"),
            entry("ls", "file1.txt file2.txt"),
            entry("ls --ord=desc", "file2.txt file1.txt")));
  }

  @Test
  void test11_removeNonExisting() {
    executeTest(
        List.of(
            entry("rm non_existent.txt", "file or directory not found: non_existent.txt"),
            entry(
                "rm --recursive non_existent_dir",
                "file or directory not found: non_existent_dir")));
  }

  @Test
  void test12_createFileInSubdirectory() {
    executeTest(
        List.of(
            entry("mkdir alpha", "'alpha' directory created"),
            entry("cd alpha", "moved to directory 'alpha'"),
            entry("touch beta.txt", "'beta.txt' file created"),
            entry("ls", "beta.txt"),
            entry("pwd", "/alpha")));
  }

  @Test
  void test13_removeFileFromSubdirectory() {
    executeTest(
        List.of(
            entry("mkdir gamma", "'gamma' directory created"),
            entry("cd gamma", "moved to directory 'gamma'"),
            entry("touch delta.txt", "'delta.txt' file created"),
            entry("ls", "delta.txt"),
            entry("rm delta.txt", "'delta.txt' removed"),
            entry("ls", ""),
            entry("pwd", "/gamma")));
  }

  @Test
  void test14_removeEmptyDirectory() {
    executeTest(
        List.of(
            entry("mkdir epsilon", "'epsilon' directory created"),
            entry("ls", "epsilon"),
            entry("cd epsilon", "moved to directory 'epsilon'"),
            entry("touch epsilon.txt", "'epsilon.txt' file created"),
            entry("cd ..", "moved to directory '/'"),
            entry("rm epsilon --recursive", "'epsilon' removed"),
            entry("ls", "")));
  }

  @Test
  void test15_cannotRemoveRoot() {
    executeTest(List.of(entry("rm / --recursive", "rm: invalid name")));
  }

  @Test
  void test16_invalidMkdirName() {
    executeTest(
        List.of(
            entry("mkdir invalid name", "invalid directory name: invalid name"),
            entry("mkdir another/invalid", "invalid directory name: another/invalid")));
  }

  @Test
  void test17_invalidTouchName() {
    executeTest(
        List.of(
            entry("touch invalid file", "invalid file name"),
            entry("touch another/invalid.txt", "invalid file name")));
  }

  @Test
  void test18_touchExistingFile() {
    executeTest(
        List.of(
            entry("touch existing.txt", "'existing.txt' file created"),
            entry("touch existing.txt", "File already exists"),
            entry("ls", "existing.txt")));
  }

  @Test
  void test19_mkdirExistingDirectory() {
    executeTest(
        List.of(
            entry("mkdir existing_dir", "'existing_dir' directory created"),
            entry("mkdir existing_dir", "invalid directory name: existing_dir"),
            entry("ls", "existing_dir")));
  }

  @Test
  void test20_cdUpFromRoot() {
    executeTest(List.of(entry("cd ..", "moved to directory '/'")));
  }

  @Test
  void test21_cdAbsolutePath() {
    executeTest(
        List.of(
            entry("mkdir a", "'a' directory created"),
            entry("cd a", "moved to directory 'a'"),
            entry("mkdir b", "'b' directory created"),
            entry("cd /a", "moved to directory 'a'"),
            entry("pwd", "/a")));
  }

  @Test
  void test22_rmFileInCurrentDirectory() {
    executeTest(
        List.of(
            entry("touch to_remove.txt", "'to_remove.txt' file created"),
            entry("ls", "to_remove.txt"),
            entry("rm to_remove.txt", "'to_remove.txt' removed"),
            entry("ls", "")));
  }

  @Test
  void test23_rmFileInSubdirectoryFromRoot() {
    executeTest(
        List.of(
            entry("mkdir sub", "'sub' directory created"),
            entry("cd sub", "moved to directory 'sub'"),
            entry("touch sub_file.txt", "'sub_file.txt' file created"),
            entry("cd ..", "moved to directory '/'"),
            entry("rm sub_file.txt", "file or directory not found: sub_file.txt")));
  }

  @Test
  void test24_rmDirectoryWithFileInsideRecursive() {
    executeTest(
        List.of(
            entry("mkdir folder_to_delete", "'folder_to_delete' directory created"),
            entry("cd folder_to_delete", "moved to directory 'folder_to_delete'"),
            entry("touch inside.txt", "'inside.txt' file created"),
            entry("cd ..", "moved to directory '/'"),
            entry("rm --recursive folder_to_delete", "'folder_to_delete' removed"),
            entry("ls", "")));
  }
}
