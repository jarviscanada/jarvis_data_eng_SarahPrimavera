package ca.jrvs.apps.jdbc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PositionDAOTest {

  private DatabaseConnectionManager dcm;
  private Connection connection;
  private PositionDAO positionDao;

  @Before
  public void setUp() throws Exception {
    dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
    connection = dcm.getConnection();
    positionDao = new PositionDAO();
    positionDao.setC(connection);
  }

  @After
  public void tearDown() throws Exception {
    if (connection != null && !connection.isClosed()) {
      connection.close();
    }
  }

  @Test
  public void save() {
    Position mockPosition = createMockPosition();

    Position result = positionDao.save(mockPosition);

    assertNotNull(result);
    assertEquals(mockPosition.getTicker(), result.getTicker());
    assertEquals(mockPosition.getNumOfShares(), result.getNumOfShares());
    assertEquals(mockPosition.getValuePaid(), result.getValuePaid(),0.01);
  }

  @Test
  public void update() {
    Position mockPosition = createMockPosition();

    mockPosition.setNumOfShares(100);
    when(mockPosition.getNumOfShares()).thenReturn(100);

    Position result = positionDao.update(mockPosition);

    assertNotNull(result);
    assertEquals(mockPosition.getTicker(), result.getTicker());
    assertEquals(mockPosition.getNumOfShares(), result.getNumOfShares());
    assertEquals(mockPosition.getValuePaid(), result.getValuePaid(),0.01);
  }

  @Test
  public void create() {
    Position mockPosition = createMockPosition();

    Position result = positionDao.create(mockPosition);

    assertNotNull(result);
    assertEquals(mockPosition.getTicker(), result.getTicker());
    assertEquals(mockPosition.getNumOfShares(), result.getNumOfShares());
    assertEquals(mockPosition.getValuePaid(), result.getValuePaid(),0.01);
  }

  @Test
  public void findById() {
    Position mockPosition = createMockPosition();

    Optional<Position> result = positionDao.findById(mockPosition.getTicker());
    assertTrue(result.isPresent());
  }

  @Test
  public void findAll() {
    Iterable<Position> positions = positionDao.findAll();
    assertTrue(positions.iterator().hasNext());
  }

  @Test
  public void deleteById() {
    Position mockPosition = createMockPosition();

    positionDao.deleteById(mockPosition.getTicker());

    Optional<Position> result = positionDao.findById(mockPosition.getTicker());
    assertFalse(result.isPresent());
  }

  @Test
  public void deleteAll() {
    positionDao.deleteAll();
    Iterable<Position> positions = positionDao.findAll();
    assertFalse(positions.iterator().hasNext());
  }

  private Position createMockPosition() {
    Position mockPosition = mock(Position.class);

    when(mockPosition.getTicker()).thenReturn("IBM");
    when(mockPosition.getNumOfShares()).thenReturn(6);
    when(mockPosition.getValuePaid()).thenReturn(99.99);

    return mockPosition;
  }
}