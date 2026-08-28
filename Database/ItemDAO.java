import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAO{
    private final Connection connection;

    public ItemDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Item item) throws SQLException {
        // Exclude AUTO_INCREMENT, STORED, and DEFAULT columns from INSERT
        String sql = "INSERT INTO Item (BoxID, Name, Description, Quantity, UnitWeight) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getBoxId());
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getDescription());
            stmt.setInt(4, item.getQuantity());
            stmt.setBigDecimal(5, item.getUnitWeight());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setItemId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public Optional<Item> findById(int itemId) throws SQLException {
        String sql = "SELECT * FROM Item WHERE ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToItem(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Item> findByBoxId(int boxId) throws SQLException {
        String sql = "SELECT * FROM Item WHERE BoxID = ?";
        List<Item> items = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boxId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs));
                }
            }
        }
        return items;
    }

    public List<Item> findAll() throws SQLException {
        String sql = "SELECT * FROM Item";
        List<Item> items = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        }
        return items;
    }

    @Override
    public boolean update(Item item) throws SQLException {
        // TotalWeight is STORED and recalculates automatically
        String sql = "UPDATE Item SET BoxID = ?, Name = ?, Description = ?, Quantity = ?, UnitWeight = ? WHERE ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, item.getBoxId());
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getDescription());
            stmt.setInt(4, item.getQuantity());
            stmt.setBigDecimal(5, item.getUnitWeight());
            stmt.setInt(6, item.getItemId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int itemId) throws SQLException {
        String sql = "DELETE FROM Item WHERE ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Helper method to map ResultSet rows to an Item object
    private Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItemId(rs.getInt("ItemID"));
        item.setBoxId(rs.getInt("BoxID"));
        item.setName(rs.getString("Name"));
        item.setDescription(rs.getString("Description"));
        item.setQuantity(rs.getInt("Quantity"));
        item.setUnitWeight(rs.getBigDecimal("UnitWeight"));
        item.setTotalWeight(rs.getBigDecimal("TotalWeight")); // Read calculated column
        item.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return item;
    }
}