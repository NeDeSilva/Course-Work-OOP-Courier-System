import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Phase5SmokeTest {
    // Small smoke test that exercises model creation and JSON export without requiring SQLite.
    public static void main(String[] args) throws Exception {
        ItemBox box = new ItemBox();
        box.addItem(new Items("T-1", "Test Item", "desc", 1, 1, 9.99f, 0, 5));

        List<Person> people = new ArrayList<>();
        people.add(new Customer("C-1", "Alice", 28, "Addr", "071-1111111", "alice@example.com", "alice", "pwd"));
        people.add(new Seller("S-1", "Bob", 34, "Shop St", "072-2222222", "bob@shop.com", "bob", "pwd", "Bob's Shop"));

        Path out = Path.of("phase5-smoke.json");
        if (Files.exists(out)) Files.delete(out);

        JSONUtil.writeJson(out.toString(), box, people);
        if (!Files.exists(out)) throw new RuntimeException("Smoke test failed: JSON not produced");
        System.out.println("Smoke test passed — wrote " + out.toAbsolutePath());
    }
}
