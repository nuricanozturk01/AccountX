package com.example.accountx.HibernateConfiguration;

import com.example.accountx.App;
import com.example.accountx.Entity.*;
import com.example.accountx.Entity.DateMapper;
import com.example.accountx.util.Constant;
import com.example.accountx.util.UtilFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.spi.ServiceException;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeSet;

@SuppressWarnings("all")
public class SessionFactoryManager
{
    public static SessionFactory factory;
    static {
        try {
            factory = new Configuration()
                    .configure(Constant.HIBERNATE_CONFIGURATION_FILE)
                    .addAnnotatedClass(CostForm.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(BankFlow.class)
                    .addAnnotatedClass(CostType.class)
                    .addAnnotatedClass(OffDay.class)
                    .addAnnotatedClass(DateMapper.class)
                    .addAnnotatedClass(OffDayType.class)
                    .addAnnotatedClass(AdvanceAmount.class)
                    .addAnnotatedClass(KDV.class)
                    .addAnnotatedClass(TreasureFlow.class)
                    .addAnnotatedClass(OtherFlow.class)
                    .addAnnotatedClass(FundFlow.class)
                    .configure()
                    .buildSessionFactory();
        }
        catch (ServiceException ex) {
            try
            {
                var loader = new FXMLLoader(App.class.getResource("baglanti_hatasi_form.fxml"));
                UtilFX.initStage("Veritabanı Bağlantı Hatası...", new Stage(), new Scene(loader.load()), false, Constant.ICON);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Session getSession()
    {
        return factory.getCurrentSession();
    }


    @SuppressWarnings("all")
    public static List<CostForm> filterDateCostForms(LocalDate from, LocalDate to)
    {
        Session session = getSession();
        try
        {
            session.beginTransaction();

            Query<CostForm> costForms = session.createSQLQuery("CALL filterByDateCostForm(:_from,:_to);")
                    .addEntity(CostForm.class)
                    .setParameter("_from", DateTimeFormatter.ISO_LOCAL_DATE.format(from))
                    .setParameter("_to", DateTimeFormatter.ISO_LOCAL_DATE.format(to));

            return costForms.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static List<AdvanceAmount> getAdvanceAmounts(int user_pk_id, int year, int month)
    {
        Session session = getSession();

        try {

            session.beginTransaction();

            Query<AdvanceAmount> advanceAmount = session.createSQLQuery("CALL getAdvanceAmounts(:user_pk_id,:year,:month);")
                    .addEntity(AdvanceAmount.class)
                    .setParameter("user_pk_id", user_pk_id)
                    .setParameter("year", year)
                    .setParameter("month", month);

            return advanceAmount.getResultList();
        }
        catch (NoResultException ignored)
        {
            return null;
        }
        finally
        {
            session.close();
        }
    }



    public static List<AdvanceAmount> getAdvanceAmountsByYear(int user_pk_id, int year)
    {
        Session session = getSession();

        try {

            session.beginTransaction();

            Query<AdvanceAmount> advanceAmount = session.createSQLQuery("CALL getAdvanceAmountsByYear(:user_pk_id,:year);")
                    .addEntity(AdvanceAmount.class)
                    .setParameter("user_pk_id", user_pk_id)
                    .setParameter("year", year);

            return advanceAmount.getResultList();
        }
        catch (NoResultException ignored)
        {
            return null;
        }
        finally
        {
            session.close();
        }
    }





    public static AdvanceAmount getAdvanceAmountByUserByDate(int user_pk_id, int year, int month, int day)
    {
        Session session = getSession();

        try {

            session.beginTransaction();

            Query<AdvanceAmount> advanceAmount = session.createSQLQuery("CALL getAdvanceAmountByDate(:user_pk_id,:year,:month,:day);")
                    .addEntity(AdvanceAmount.class)
                    .setParameter("user_pk_id", user_pk_id)
                    .setParameter("year", year)
                    .setParameter("day", day)
                    .setParameter("month", month);

            return advanceAmount.getSingleResult();
        }
        catch (NoResultException ignored){
            return null;
        }
        finally {
            session.close();
        }
    }



    public static List<BankFlow> filterDateBankFlows(LocalDate from, LocalDate to)
    {
        Session session = getSession();

        try {
            var start = DateTimeFormatter.ISO_LOCAL_DATE.format(from);
            var end = DateTimeFormatter.ISO_LOCAL_DATE.format(to);

            session.beginTransaction();

            Query<BankFlow> bankFlows = session.createSQLQuery("CALL filterByDateBankFlow(:_from,:_to);")
                    .addEntity(BankFlow.class)
                    .setParameter("_from", start)
                    .setParameter("_to", end);

            return bankFlows.getResultList();
        }
        finally {
            session.close();
        }
    }
    public static List<BankFlow> filterCostTypeBankFlows(String type)
    {
        Session session = getSession();

        try {
            session.beginTransaction();

            Query<BankFlow> bankFlows = session.createSQLQuery("CALL filterByCostTypeBankFlow(:str);")
                    .addEntity(BankFlow.class)
                    .setParameter("str", type);

            return bankFlows.getResultList();
        }
        finally {
            session.close();
        }
    }

    @SuppressWarnings("all")
    public static List<CostForm> filterCostTypeCostFlows(String type)
    {
        Session session = getSession();

        try {
            session.beginTransaction();

            Query<CostForm> costForms = session.createSQLQuery("CALL filterByCostTypeCostForm(:str);")
                    .addEntity(CostForm.class)
                    .setParameter("str", type);

            return costForms.getResultList();
        }
        finally {
            session.close();
        }
    }
    public static List<CostForm> filterCostTypeCostFormsExactMatch(String type)
    {
        Session session = getSession();

        try {
            session.beginTransaction();

            Query<CostForm> costForms = session.createSQLQuery("CALL filterByNameCostTypeExactMatch(:str);")
                    .addEntity(CostForm.class)
                    .setParameter("str", type);

            return costForms.getResultList();
        }
        finally {
            session.close();
        }
    }
    public static List<CostForm> filterNameCostForms(String type)
    {
        Session session = getSession();

        try {
            session.beginTransaction();

            Query<CostForm> costFlows = session.createSQLQuery("CALL filterByNameCostForm(:str);")
                    .addEntity(CostForm.class)
                    .setParameter("str", type);

            return costFlows.getResultList();
        }
        finally {
            session.close();
        }
    }
    public static List<BankFlow> filterNameBankFlows(String type)
    {
        Session session = getSession();

        try {
            session.beginTransaction();

            Query<BankFlow> bankFlows = session.createSQLQuery("CALL filterByNameBankFlow(:str);")
                    .addEntity(BankFlow.class)
                    .setParameter("str", type);

            return bankFlows.getResultList();
        }
        finally {
            session.close();
        }
    }
    public static List<User> filterNameUser(String type)
    {
        Session session = getSession();

        try {
            session.beginTransaction();

            Query<User> users = session.createSQLQuery("CALL filterByNameUser(:str);")
                    .addEntity(User.class)
                    .setParameter("str", type);

            return users.getResultList();
        }
        finally {
            session.close();
        }
    }
    public static List<CostType> filterNameCostType(String type)
    {
        Session session = getSession();

        try {
            session.beginTransaction();

            Query<CostType> types = session.createSQLQuery("CALL filterByNameCostType(:str);")
                    .addEntity(CostType.class)
                    .setParameter("str", type);

            return types.getResultList();
        }
        finally {
            session.close();
        }
    }
    public static <T> void addAll(List<T> objects) {
        var session = getSession();
        session.beginTransaction();
        objects.forEach(session::save);
        session.getTransaction().commit();
        session.close();
    }

    public static <T> void updateAll(TreeSet<T> objects) {
        var session = getSession();
        session.beginTransaction();
        objects.forEach(session::update);
        session.getTransaction().commit();
        session.close();
    }
    public static void add(Object obj)
    {
        Session session = getSession();
        session.beginTransaction();
        session.persist(obj);
        session.getTransaction().commit();
        session.close();
    }

    public static void addSaveOrUpdate(Object obj)
    {
        Session session = getSession();
        session.beginTransaction();
        session.saveOrUpdate(obj);
        session.getTransaction().commit();
        session.close();
    }

    public static<T> T get(Class<T> var, Serializable serializable)
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            return session.get(var,serializable);
        }
        finally {
            session.close();
        }
    }


    public static CostForm getCostForm()
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM CostForm ORDER BY cost_form_pk_id DESC");
            query.setMaxResults(1);

            return (CostForm) query.uniqueResult();
        }
        finally {
            session.close();
        }
    }

    public static List<User> getUserList()
    {
        Session session = getSession();
        try{
            session.beginTransaction();
            Query<User> users = session.createQuery("FROM User", User.class);
            return users.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static<T> void update(T obj)
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(obj);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
    public static List<KDV> getKDVs()
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<KDV> kdvs = session.createQuery("FROM KDV", KDV.class);
            return kdvs.getResultList();
        }
        finally {
            session.close();
        }
    }
    public static List<Integer> getDistinctYears(int id)
    {
        Session session = getSession();
        try {
            session.beginTransaction();

            Query<Integer> years = session.createSQLQuery("CALL getYearByUser(:id);").setParameter("id", id);

            return years.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static BigDecimal getSumOfCostForm(Integer year, int id)
    {
        Session session = getSession();
        try {
            session.beginTransaction();

            Query<BigDecimal> sum = session.createSQLQuery("CALL getSumOfCostFormByYear(:year,:id);")
                    .setParameter("year", year)
                    .setParameter("id", id);

            return sum.getSingleResult();
        }
        catch (NoResultException ex)
        {
            return BigDecimal.ZERO;
        }
        finally {
            session.close();
        }
    }

    @SuppressWarnings("all")
    public static List<DateMapper> sortCostFormsByDate(int id)
    {
        Session session = getSession();

        try {
            session.beginTransaction();

            Query<DateMapper> types = session.createSQLQuery("CALL getDatesGrouped(:id);")
                    .addEntity(DateMapper.class)
                    .setParameter("id", id);
            types.executeUpdate();

            Query<DateMapper> result = session.createQuery("FROM DateMapper ORDER BY year, month");

            return result.getResultList();
        }
        finally {
            session.close();
        }
    }

    @SuppressWarnings("all")
    public static List<CostForm> getCostFormsWithMonth(int id, DateMapper dateMapper)
    {
        Session session = getSession();
        try
        {
            session.beginTransaction();

            Query<CostForm> costForms = session.createSQLQuery("CALL getCostFormsWithMonth(:month,:year, :id);")
                    .addEntity(CostForm.class)
                    .setParameter("month", dateMapper.getMonth())
                    .setParameter("year", dateMapper.getYear())
                    .setParameter("id", id);

            return costForms.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static void cleanDateMapper()
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<CostType> costTypes = session.createSQLQuery("TRUNCATE TABLE date_mapper;");
            costTypes.executeUpdate();
        }
        finally {
            session.close();
        }
    }

    public static List<OffDayType> getReasonList()
    {
        Session session = getSession();
        try{
            session.beginTransaction();
            Query<OffDayType> types = session.createQuery("FROM OffDayType ", OffDayType.class);
            return types.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static List<OffDay> getOffDays(int user_pk_id, int year, Months_TR month)
    {
        Session session = getSession();
        try
        {
            session.beginTransaction();

            Query<OffDay> offDays = session.createSQLQuery("CALL getOffDays(:id,:year, :month);")
                    .addEntity(OffDay.class)
                    .setParameter("id", user_pk_id)
                    .setParameter("year", year)
                    .setParameter("month", month.ordinal());

            return offDays.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static BigDecimal getSumOfTypes(String type)
    {
        Session session = getSession();
        try
        {
            session.beginTransaction();

            Query<BigDecimal> sum = session.createSQLQuery("CALL getSumOfTypes(:type);")
                    .setParameter("type", type);

            return sum.getSingleResult();
        }
        catch (NoResultException ignored)
        {
            return BigDecimal.ZERO;
        }
        finally {
            session.close();
        }
    }

    public static BigDecimal getSumOfTypes(String type, int month, int year) {
        Session session = getSession();
        try {
            session.beginTransaction();

            Query<BigDecimal> sum = session.createSQLQuery("CALL getSumOfTypesWithMonthAndYear(:type, :month, :year);")
                    .setParameter("type", type)
                    .setParameter("year", year)
                    .setParameter("month", month);

            return sum.getSingleResult();
        }
        catch (NoResultException ignored) {
            session.close();
            return null;
        }
        finally {
            session.close();
        }
    }
    public static BigDecimal getSumOfTypesBankFlow(String type)
    {
        Session session = getSession();
        try
        {
            session.beginTransaction();

            Query<BigDecimal> sum = session.createSQLQuery("CALL getSumOfTypesBankFlow(:type);")
                    .setParameter("type", type);

            return sum.getSingleResult();
        }
        catch (NoResultException ignored) {
            return BigDecimal.ZERO;
        }
        finally {
            session.close();
        }
    }
    public static BigDecimal getSumOfTypesBankFlow(String type, int month) {
        Session session = getSession();
        try {
            session.beginTransaction();

            Query<BigDecimal> sum = session.createSQLQuery("CALL getSumOfTypesBankFlowWithMonth(:type, :month);")
                    .setParameter("type", type)
                    .setParameter("month", month);

            return sum.getSingleResult();
        }
        catch (NoResultException ignored) {
            return null;
        }
        finally {
            session.close();
        }
    }

    public static BigDecimal getSumOfTypesBankFlow(String type, int year, int month) {
        Session session = getSession();
        try {
            session.beginTransaction();

            Query<BigDecimal> sum = session.createSQLQuery("CALL getSumOfTypesBankFlowWithMonthAndYear(:type, :year, :month);")
                    .setParameter("type", type)
                    .setParameter("year", year)
                    .setParameter("month", month);

            return sum.getSingleResult();
        }
        catch (NoResultException ignored) {
            return null;
        }
        finally {
            session.close();
        }
    }
    public static BigDecimal getSumOfAllTypes() {
        Session session = getSession();
        try {
            session.beginTransaction();

            Query<BigDecimal> sum = session.createSQLQuery("CALL getSumOfAllTypes();");

            return sum.getSingleResult();
        }
        finally {
            session.close();
        }
    }
    public static BigDecimal getSumofAllTypesBankFlow() {
        Session session = getSession();
        try {
            session.beginTransaction();

            Query<BigDecimal> sum = session.createSQLQuery("CALL getSumofAllTypesBankFlow();");

            return sum.getSingleResult();
        }
        finally {
            session.close();
        }
    }
    public static List<CostForm> getCostFormList()
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<CostForm> costForms = session.createQuery("FROM CostForm ", CostForm.class);
            return costForms.getResultList();
        }finally {
            session.close();
        }
    }

    public static List<CostForm> getCostFormList(int max)
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<CostForm> costForms = session.createSQLQuery("SELECT * FROM cost_form LIMIT :lim")
                    .addEntity(CostForm.class)
                    .setParameter("lim", max);
            return costForms.getResultList();
        }finally {
            session.close();
        }
    }

    public static List<OffDay> getOffDaysByYearAndUser(int user_pk_id, int year)
    {
        Session session = getSession();
        try
        {
            session.beginTransaction();

            Query<OffDay> offDays = session.createSQLQuery("CALL getOffDaysByYearAndUser(:year, :id);")
                    .addEntity(OffDay.class)
                    .setParameter("id", user_pk_id)
                    .setParameter("year", year);

            return offDays.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static List<FundFlow> getFundFlows() {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<FundFlow> fundFlows = session.createQuery("FROM FundFlow ", FundFlow.class);
            return fundFlows.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static List<TreasureFlow> getTreasureFlows()
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<TreasureFlow> treasures = session.createQuery("FROM TreasureFlow ", TreasureFlow.class);
            return treasures.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static List<OtherFlow> getOtherFlows() {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<OtherFlow> otherFlows = session.createQuery("FROM OtherFlow", OtherFlow.class);
            return otherFlows.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static BigDecimal getSumOfCostsofPersonWithMonth(int user_pk_id, Integer year, int month)
    {
        Session session = getSession();
        try
        {
            session.beginTransaction();

            Query<BigDecimal> sum = session.createSQLQuery("CALL getSumOfCostsofPersonWithMonth(:user_pk_id, :year,:month);")
                    .setParameter("user_pk_id", user_pk_id)
                    .setParameter("year", year)
                    .setParameter("month", month);

            return sum.getSingleResult();
        }
        catch (NoResultException ignored)
        {
            return BigDecimal.ZERO;
        }
        finally
        {
            session.close();
        }
    }

    public static boolean removeOffDays(int user_pk_id, LocalDate startDate, LocalDate finishDate) {

        Session session = getSession();
        try {
            session.beginTransaction();


            Query query = session.createSQLQuery("CALL deleteOffDays(:user_pk_id, :startDate,:finishDate);")
                    .setParameter("user_pk_id", user_pk_id)
                    .setParameter("startDate", startDate)
                    .setParameter("finishDate", finishDate);
            query.executeUpdate();
            return true;
        }
        catch (NoResultException ignored) {
            return false;
        }
        finally {
            session.close();
        }
    }


    public static boolean removeBankFlowsByDateRange(LocalDate startDate, LocalDate finishDate) {

        Session session = getSession();
        try {
            session.beginTransaction();


            Query query = session.createSQLQuery("CALL deleteBankFlowsByDateRange(:startDate,:finishDate);")
                    .setParameter("startDate", startDate)
                    .setParameter("finishDate", finishDate);
            query.executeUpdate();
            return true;
        }
        catch (NoResultException ignored) {
            return false;
        }
        finally {
            session.close();
        }
    }


    public static boolean removeCostFormsByDateRange(LocalDate startDate, LocalDate finishDate) {

        Session session = getSession();
        try {
            session.beginTransaction();


            Query query = session.createSQLQuery("CALL deleteCostFormsByDateRange(:startDate,:finishDate);")
                    .setParameter("startDate", startDate)
                    .setParameter("finishDate", finishDate);
            query.executeUpdate();
            return true;
        }
        catch (NoResultException ignored) {
            return false;
        }
        finally {
            session.close();
        }
    }

    public static CostType getCostType(String costType) {
        Session session = getSession();
        try {
            session.beginTransaction();

            Query<CostType> query = session.createSQLQuery("CALL getCostType(:type);")
                    .addEntity(CostType.class)
                    .setParameter("type", costType);

            return query.getSingleResult();
        }
        catch (NoResultException ignored) {
            return null;
        }
        finally {
            session.close();
        }
    }

    public static void delete(AdvanceAmount existingValue)
    {
        var session = getSession();

        try {
            session.beginTransaction();
            session.delete(existingValue);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public static void remove(Object item)
    {
        var session = getSession();

        try
        {
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
        }
        finally
        {
            session.close();
        }
    }

    public static List<BankFlow> FilteredBankFlows(String queryStr)
    {

        Session session = getSession();
        try {
            session.beginTransaction();


            Query<BankFlow> query = session.createSQLQuery(queryStr).addEntity(BankFlow.class);

            return query.getResultList();
        }
        catch (NoResultException ignored) {
            return null;
        }
        finally {
            session.close();
        }
    }

    public static List<CostForm> FilteredCostForms(String queryStr)
    {
        Session session = getSession();
        try {
            session.beginTransaction();


            Query<CostForm> query = session.createSQLQuery(queryStr).addEntity(CostForm.class);

            return query.getResultList();
        }
        catch (NoResultException ignored) {
            return null;
        }
        finally {
            session.close();
        }
    }


    // Add many bank flows
    private void addBankFlows(List<BankFlow> bankFlows)
    {
        Session session = getSession();
        try
        {
            session.beginTransaction();
            bankFlows.forEach(session::saveOrUpdate);
            session.getTransaction().commit();
        }
        finally
        {
            session.close();
        }
    }
    public static List<CostType> getCostTypes()
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<CostType> costTypes = session.createQuery("FROM CostType", CostType.class);
            return costTypes.getResultList();
        }
        finally {
            session.close();
        }
    }
    public static List<BankFlow> getBankFlows()
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<BankFlow> bankFlows = session.createQuery("FROM BankFlow", BankFlow.class);
            return bankFlows.getResultList();
        }
        finally {
            session.close();
        }
    }

    public static List<BankFlow> getBankFlows(int max)
    {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query<BankFlow> bankFlows = session.createSQLQuery("SELECT * FROM bank_flow LIMIT :lim")
                    .addEntity(BankFlow.class)
                    .setParameter("lim", max);
            return bankFlows.getResultList();
        }
        finally {
            session.close();
        }
    }
}
