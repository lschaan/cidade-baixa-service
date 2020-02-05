# cidade-baixa-service

#### What?
This service aims to provide an unified API to check for future events on Porto Alegre's Cidade Baixa, allowing filters for a specific club, date or maximum ticket price.

#### How?
It either consumes the club's own API, originally consumed by their website or parses through the club website's HTML. The second option is only possible when there is a static location in the website for ticket prices.

#### Why?
I might use this in order to learn a bit about frontend and build a simple page. Or maybe I'll create another service that consumes this one and notifies me when there's an open bar. Who knows. 

### Consuming the service

This should be pretty straightfoward. There is only on route so far, `/api/v1/list`. The response is a PartyResponse json with the following format:
```json
{
  "content": [
    {
      "club": "CUCKO",
      "date": "string",
      "message": "string",
      "openBar": true,
      "partyName": "string",
      "tickets": [
        {
          "dueDate": "string",
          "price": 0,
          "type": "ON_SITE"
        }
      ]
    }
  ]
}
```

Here's an example when filtering for `date = 2020-02-07`
```json
{
  "content": [
    {
      "partyName": "Baile do Vapo Vapo | Skol Beats & Tropical Gin 2x @Cucko",
      "date": "2020-02-07",
      "openBar": false,
      "tickets": [
        {
          "price": 25,
          "type": "IN_ADVANCE",
          "dueDate": "2020-02-07"
        },
        {
          "price": 35,
          "type": "ON_SITE",
          "dueDate": "2020-02-07"
        }
      ],
      "message": null,
      "club": "CUCKO"
    },
    {
      "partyName": "Bloco da Nuvem | OPEN BAR à Fantasia - 08/02",
      "date": "2020-02-07",
      "openBar": true,
      "tickets": [
        {
          "price": 55,
          "type": null,
          "dueDate": "2020-02-07"
        }
      ],
      "message": null,
      "club": "NUVEM"
    }
  ]
}
```

To keep in mind: 

- All parameters in the `/api/v1/list` route are optional. 
- The tickets for the party will only show if they are below maxValue. 
- If no tickets are above maxValue, the party will not be displayed.