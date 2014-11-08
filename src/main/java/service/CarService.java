package service;

import entity.Car;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("/carService")
public class CarService {

    private static Map<Integer, Car> map = new ConcurrentHashMap<>();
    private static AtomicInteger atomicInteger = new AtomicInteger(1000);

    @PUT
    @Path("/add/{make},{model},{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCar(@PathParam("make") String make,
                           @PathParam("model") String model,
                           @PathParam("year") String year) {

        /**
         *    need to add validation before I actually go about adding the car.
         *    1. check if the year is a valid date.
         *    2. if the year can be converted into a format as specified by the Simple Date Formatter
         * */
        try {
            int carId = atomicInteger.getAndIncrement();
            Car c = new Car();
            c.setId(carId);
            c.setMake(make);
            c.setModel(model);
            try {
                c.setYearOfManufacture(new SimpleDateFormat("dd/mm/yyyy").parse(year));
            } catch (ParseException e) {
                // the check would have been done before as per the comments above.
            }
            // a better option will be to user JodaTime
            c.setEntryDate(new Date());

            map.put(carId, c);
            return Response.status(200).status(Response.Status.CREATED).build();
        } catch (Exception e) {
            // log error.
            /**
             * May be have multiple error handling and return appropriate error messages
             */
            return Response.status(BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/retrieve/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCar(@PathParam("id") int id) {
        try {
            Car c;
            if (map.containsKey(id) && null != map.get(id)) {
                c = map.get(id);
                return Response.status(200).entity(c).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(BAD_REQUEST).build();
        }

    }

    @DELETE
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeCar(@PathParam("id") int id) {
        try {
            if (map.containsKey(id) && null != map.get(id)) {
                map.remove(id);
                return Response.status(Response.Status.OK).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/update/{make},{model},{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCar(@PathParam("make") String make,
                              @PathParam("model") String model,
                              @PathParam("year") String year) {

        /**
         *    need to add validation before I actually go about adding the car.
         *    1. check if the year is a valid date.
         *    2. if the year can be converted into a format as specified by the Simple Date Formatter
         * */
        try {
            int carId = atomicInteger.get();
            Car c = map.get(carId);
            if (null != c) {
                c.setMake(make);
                c.setModel(model);
                try {
                    c.setYearOfManufacture(new SimpleDateFormat("dd/mm/yyyy").parse(year));
                } catch (ParseException e) {
                    // the check would have been done before as per the comments above.
                }
                // a better option will be to user JodaTime
                c.setEntryDate(new Date());

                map.put(carId, c);
                return Response.status(200).status(Response.Status.OK).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            // log error.
            /**
             * May be have multiple error handling and return appropriate error messages
             */
            return Response.status(BAD_REQUEST).build();
        }

    }

}
