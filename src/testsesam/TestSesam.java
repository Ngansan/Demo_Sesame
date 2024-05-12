
package testsesam;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class TestSesam {
    public static void main(String[] args) {
        Repository repository = new SailRepository(new MemoryStore());
        repository.init();

        try (RepositoryConnection connection = repository.getConnection()) {
            ValueFactory vf = SimpleValueFactory.getInstance();

            // Tạo các thông tin RDF
            connection.add(vf.createIRI("https://www.oreilly.com/library/view/learning-sparql/9781449311285/ch01.html"), DC.TITLE, vf.createLiteral("SPARQL - The Definitive Guide"));
            connection.add(vf.createIRI("https://www.learningsparql.com/"), DC.TITLE, vf.createLiteral("Learning SPARQL"));

            // Những cuốn sách có từ 'SPARQL' trong tiêu đề
            String queryString = "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
                                 "SELECT ?book\n" +
                                 "WHERE {\n" +
                                 "    ?book dc:title ?title ." +
                                 "    FILTER regex(?title, \"SPARQL\") " +
                                 "}";
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();

            // In ra kết quả
            while (result.hasNext()) {
                System.out.println(result.next().getValue("book"));
            }
        }
        repository.shutDown();
    }
}




