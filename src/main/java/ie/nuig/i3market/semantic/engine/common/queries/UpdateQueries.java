package ie.nuig.i3market.semantic.engine.common.queries;


import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import org.apache.http.protocol.HttpContext;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.modify.UpdateProcessRemoteForm;
import org.apache.jena.update.UpdateException;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class UpdateQueries {

    private static final Logger log = LoggerFactory.getLogger(UpdateQueries.class);
    static QueryExecution qExe;


    public static String insertQuery(Statement statement, String graph){

        ParameterizedSparqlString parameterizedSparqlString = new ParameterizedSparqlString();
        parameterizedSparqlString.append("INSERT DATA");
        parameterizedSparqlString.append(" {");
        if(graph !=null) {
            parameterizedSparqlString.append(" GRAPH ");
            parameterizedSparqlString.append("<"+graph+">");
            parameterizedSparqlString.append(" {");
        }
        parameterizedSparqlString.append(statement_As_NTriple(statement));
        if(graph!= null)
            parameterizedSparqlString.append(" }");
        parameterizedSparqlString.append(" }");

        return parameterizedSparqlString.toString();
    }

    private static String statement_As_NTriple (Statement statement) {

       try {
           Model model = ModelFactory.createDefaultModel();
           Resource resource = model.createResource(statement.getSubject().toString());
           resource.addProperty(statement.getPredicate(), statement.getObject());

           StringWriter out = new StringWriter();
           model.write(out, "N-Triples");

           return out.toString();
       }catch(Exception e){
           e.getMessage();
       }
       return  null;
    }


    public static void exec(UpdateRequest request, String endpoint, HttpContext context){

       try {
           UpdateProcessor updateProcessor = UpdateExecutionFactory.createRemoteForm(request, endpoint);
           ((UpdateProcessRemoteForm) updateProcessor).setHttpContext(context);
           updateProcessor.execute();
       }catch (UpdateException e){
           e.getMessage();
       }

    }

    public static String  updateQuery(Statement statement, String endpoint ,String graph){

        if(statement== null)
            throw new NullPointerException();

        Resource subject = statement.getSubject();
        Property predicate = statement.getPredicate();
        RDFNode object = statement.getObject();

        if(object==null || object.toString().isEmpty())
            return null;

        String select="SELECT * WHERE { \n" +
                "?s ?p ?o. \n" +
                "VALUES (?s ?p ) {(<" + subject + "> <" + predicate + ">) } \n" +
                "}";

        Query selectQry =QueryFactory.create(select);
        ResultSet resultSet = execSelect(selectQry, endpoint, graph );

        if(!resultSet.hasNext()) {
            qExe.close();
            return insertQuery(statement, graph);
        } else {
            // even we are not using resultset, we have to close the resource
            qExe.close();

            // here theme will be skipped, otherwise current implementation  will remove
            // or overwrite all the existing themes in database

            if (predicate.toString() != Vocabulary.DCAT.Properties.theme) {
                String query = "WITH <" + graph + "> \n" +
                        "DELETE {?s ?p ?o} \n" +
                        "INSERT { ?s ?p " + SPARQLUtil.prepareResourceOrLiteral(object.toString()) + " } \n" +
                        "WHERE { \n" +
                        "?s ?p ?o. \n" +
                        "VALUES (?s ?p ) {(<" + subject + "> <" + predicate + ">) } \n" +
                        "}";
                return query;
            }
        }

        return null;
    }


    public static  ResultSet execSelect (Query qry, String endpoint, String graph){

        Query query = QueryFactory.create(qry);
        ResultSet resultSet = null;
        log.info("query is executed at endpoint {}", endpoint);
        log.info("the actual query is {}", qry);
        try {qExe = QueryExecutionFactory.sparqlService(endpoint, query, graph);
            //qExe.setTimeout(1500);
            resultSet = qExe.execSelect();
        }catch(QueryException exp){
            //throw  new QueryException(exp);
            return null;
        }
        return resultSet;
    }

    public  void closeQueryExec(){
        qExe.close();
    }

}
