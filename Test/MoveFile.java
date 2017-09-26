package Test;

import java.io.File;

public class MoveFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try{
			File folder = new File("File/output");
			File[] allFiles = folder.listFiles();
			for (int i = 0; i < allFiles.length; i++){
				if (allFiles[i].isFile()){
					String fileName = allFiles[i].getName();
					int index = fileName.lastIndexOf("_");
					
					String newFileName = fileName.substring(0, index) + "_Layered" + fileName.substring(index, fileName.length());
					
					allFiles[i].renameTo(new File("File/output/" + newFileName));
				}
			}
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
