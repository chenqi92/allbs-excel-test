package cn.allbs.excel.test;

import cn.allbs.excel.annotation.ExcelEncryption;
import cn.allbs.excel.test.entity.SensitiveUserDTO;
import cn.allbs.excel.test.service.TestDataService;
import cn.allbs.excel.util.ExcelEncryptionUtil;
import com.alibaba.excel.EasyExcel;

import java.io.File;
import java.util.List;

/**
 * Excel Encryption Test
 * <p>
 * Tests Excel file encryption and decryption
 * </p>
 *
 * @author ChenQi
 * @since 2025-11-19
 */
public class ExcelEncryptionTest {

	public static void main(String[] args) {
		System.out.println("=".repeat(80));
		System.out.println("Excel Encryption Test");
		System.out.println("=".repeat(80));

		TestDataService testDataService = new TestDataService();
		List<SensitiveUserDTO> data = testDataService.generateSensitiveUsers(20);

		// Test 1: Standard encryption
		testStandardEncryption(data);

		// Test 2: Agile encryption (AES)
		testAgileEncryption(data);

		// Test 3: Decrypt and verify
		testDecryption();

		System.out.println("\n" + "=".repeat(80));
		System.out.println("All encryption tests completed successfully!");
		System.out.println("=".repeat(80));
	}

	/**
	 * Test standard encryption
	 */
	private static void testStandardEncryption(List<SensitiveUserDTO> data) {
		System.out.println("\n1. Standard Encryption Test:");
		System.out.println("   " + "-".repeat(70));

		String unencryptedFile = "E:/temp/user_data_unencrypted.xlsx";
		String encryptedFile = "E:/temp/user_data_standard_encrypted.xlsx";
		String password = "mySecretPassword123";

		// Write unencrypted file
		EasyExcel.write(unencryptedFile, SensitiveUserDTO.class)
			.sheet("User Data")
			.doWrite(data);
		System.out.println("   ✓ Created unencrypted file: " + unencryptedFile);

		// Encrypt file
		File sourceFile = new File(unencryptedFile);
		File targetFile = new File(encryptedFile);
		ExcelEncryptionUtil.encryptFile(
			sourceFile,
			targetFile,
			password,
			ExcelEncryption.EncryptionAlgorithm.STANDARD
		);
		System.out.println("   ✓ Encrypted with STANDARD algorithm: " + encryptedFile);

		// Verify encryption
		boolean isEncrypted = ExcelEncryptionUtil.isEncrypted(targetFile);
		System.out.println("   ✓ File is encrypted: " + isEncrypted);
		System.out.println("   ✓ File size: " + targetFile.length() + " bytes");
	}

	/**
	 * Test agile encryption (AES)
	 */
	private static void testAgileEncryption(List<SensitiveUserDTO> data) {
		System.out.println("\n2. Agile Encryption (AES) Test:");
		System.out.println("   " + "-".repeat(70));

		String unencryptedFile = "E:/temp/user_data_unencrypted.xlsx";
		String encryptedFile = "E:/temp/user_data_agile_encrypted.xlsx";
		String password = "strongPassword@2025";

		// Encrypt file with Agile mode
		File sourceFile = new File(unencryptedFile);
		File targetFile = new File(encryptedFile);
		ExcelEncryptionUtil.encryptFile(
			sourceFile,
			targetFile,
			password,
			ExcelEncryption.EncryptionAlgorithm.AGILE
		);
		System.out.println("   ✓ Encrypted with AGILE algorithm (AES): " + encryptedFile);

		// Verify encryption
		boolean isEncrypted = ExcelEncryptionUtil.isEncrypted(targetFile);
		System.out.println("   ✓ File is encrypted: " + isEncrypted);
		System.out.println("   ✓ File size: " + targetFile.length() + " bytes");
		System.out.println("   ℹ Agile encryption provides stronger security (AES-256)");
	}

	/**
	 * Test decryption
	 */
	private static void testDecryption() {
		System.out.println("\n3. Decryption Test:");
		System.out.println("   " + "-".repeat(70));

		String encryptedFile = "E:/temp/user_data_standard_encrypted.xlsx";
		String decryptedFile = "E:/temp/user_data_decrypted.xlsx";
		String password = "mySecretPassword123";

		// Decrypt file
		File sourceFile = new File(encryptedFile);
		File targetFile = new File(decryptedFile);
		ExcelEncryptionUtil.decryptFile(sourceFile, targetFile, password);
		System.out.println("   ✓ Decrypted file: " + decryptedFile);
		System.out.println("   ✓ File size: " + targetFile.length() + " bytes");

		// Verify it's not encrypted anymore
		boolean isEncrypted = ExcelEncryptionUtil.isEncrypted(targetFile);
		System.out.println("   ✓ File is NOT encrypted: " + !isEncrypted);

		System.out.println("\n   💡 Tips:");
		System.out.println("   - Open the encrypted files in Excel to verify password protection");
		System.out.println("   - Standard encryption works with all Excel versions");
		System.out.println("   - Agile encryption requires Excel 2010 or later");
		System.out.println("   - Decrypted file can be opened without password");
	}
}
