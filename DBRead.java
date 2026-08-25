public class DBRead {
    public static void main(String[] args) throws Exception {
        DAO dao = new DAO("data.json");
        ItemBox box = dao.loadItemBox();
        System.out.println("items from DAO.loadItemBox(): " + box.getAllItems().size());
        for (Items it : box.getAllItems()) System.out.printf("%s | %s | %f | %d\n", it.itemID, it.itemName, it.itemPrice, it.stockCount);
    }
}
