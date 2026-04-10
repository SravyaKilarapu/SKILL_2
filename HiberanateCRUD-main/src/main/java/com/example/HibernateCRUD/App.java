package com.example.HibernateCRUD;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    // Disable Hibernate logs
    private static void disableHibernateLogs() {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }

    public static void main(String[] args) {

        disableHibernateLogs();

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();
        Transaction tx = null;

        try {

            /* ================= INSERT (SKILL-2) ================= */
            tx = session.beginTransaction();

            Product product = new Product();
            product.setName("Laptop");
            product.setDescription("Electronics");
            product.setPrice(72000.0);
            product.setQuantity(8);

            session.save(product);
            tx.commit();

            System.out.println("Insertion successfully completed");
            System.out.println("ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Quantity: " + product.getQuantity());
            System.out.println();


            /* ================= RETRIEVE (SKILL-2) ================= */
            Product fetchedProduct = session.get(Product.class, product.getId());

            System.out.println("Product Retrieved by ID:");
            System.out.println("ID: " + fetchedProduct.getId());
            System.out.println("Name: " + fetchedProduct.getName());
            System.out.println("Price: " + fetchedProduct.getPrice());
            System.out.println("Quantity: " + fetchedProduct.getQuantity());
            System.out.println();


            /* ================= UPDATE (SKILL-2) ================= */
            tx = session.beginTransaction();

            fetchedProduct.setName("Earphones");
            fetchedProduct.setPrice(150.0);
            fetchedProduct.setQuantity(300);

            session.update(fetchedProduct);
            tx.commit();

            System.out.println("Updated product details:");
            System.out.println("ID: " + fetchedProduct.getId());
            System.out.println("Name: " + fetchedProduct.getName());
            System.out.println("Price: " + fetchedProduct.getPrice());
            System.out.println("Quantity: " + fetchedProduct.getQuantity());
            System.out.println();


            /* ================= DELETE (SKILL-2) ================= */
            tx = session.beginTransaction();

            System.out.println("Product discontinued:");
            System.out.println("ID: " + fetchedProduct.getId());
            System.out.println("Name: " + fetchedProduct.getName());

            session.delete(fetchedProduct);
            tx.commit();

            System.out.println("Product deleted successfully");
            System.out.println();


            /* ===================================================== */
            /* ================= SKILL-3 : HQL ==================== */
            /* ===================================================== */

            /* 1. SORT PRODUCTS BY PRICE (ASCENDING) */
            System.out.println("Products sorted by price (Ascending):");

            List<Product> sortedByPrice =
                    session.createQuery(
                            "FROM Product p ORDER BY p.price ASC",
                            Product.class
                    ).getResultList();

            for (Product p : sortedByPrice) {
                System.out.println(p.getName() + " - " + p.getPrice());
            }

            System.out.println();


            /* 2. PAGINATION – FIRST 3 PRODUCTS */
            System.out.println("First 3 products (Pagination):");

            List<Product> firstThree =
                    session.createQuery("FROM Product", Product.class)
                           .setFirstResult(0)
                           .setMaxResults(3)
                           .getResultList();

            for (Product p : firstThree) {
                System.out.println(p.getId() + " " + p.getName());
            }

            System.out.println();


            /* 3. AGGREGATE – COUNT TOTAL PRODUCTS */
            Long totalProducts =
                    session.createQuery(
                            "SELECT COUNT(p) FROM Product p",
                            Long.class
                    ).getSingleResult();

            System.out.println("Total number of products: " + totalProducts);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            factory.close();
        }
    }
}
