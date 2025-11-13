*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot
Test Setup    Criar Sessao API

*** Variables ***
${TAG_ALL_TESTS}    smoke    regression
${TAG_SUCCESS}      success
${TAG_FAILURE}      failure
${TAG_MULTIPLE}     multiple
${TAG_REQUIRED}     required
${TAG_E2E}          end-to-end
${TAG_PAYMENT}      payment

*** Test Cases ***
POST-BUY-TICKET-001 - Validar compra de ticket com dados válidos
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Compra de ticket com dados válidos

    ${ticket_type_id}=    Criar_Ticket_Type_E_Capturar_ID
    ${response}=    Executar_Compra_Ticket    ${ticket_type_id}    1    200
    Validar_Response_Compra_Sucesso    ${response}

POST-BUY-TICKET-002 - Validar compra múltipla de tickets do mesmo tipo
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_MULTIPLE}
    [Documentation]    Compra de 3 tickets do mesmo tipo com holders diferentes

    ${ticket_type_id}=    Criar_Ticket_Type_E_Capturar_ID
    ${response}=    Executar_Compra_Ticket    ${ticket_type_id}    3    200
    Validar_Response_Compra_Sucesso    ${response}

POST-BUY-TICKET-003 - Validar falha ao não preencher campo obrigatório "tickets"
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}    ${TAG_REQUIRED}
    [Documentation]    Falha por campo tickets ausente no payload

    ${ticket_type_id}=    Criar_Ticket_Type_E_Capturar_ID
    ${payload}=    Criar Payload Compra Ticket Sem Tickets    ${ticket_type_id}
    ${response}=    Executar_Compra_Ticket_Com_Payload    ${payload}    400

POST-BUY-TICKET-004 - Validar falha ao não preencher campo obrigatório "payer"
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}    ${TAG_REQUIRED}
    [Documentation]    Falha por campo payer ausente no payload

    ${ticket_type_id}=    Criar_Ticket_Type_E_Capturar_ID
    ${payload}=    Criar Payload Compra Ticket Sem Payer    ${ticket_type_id}
    ${response}=    Executar_Compra_Ticket_Com_Payload    ${payload}    400

POST-BUY-TICKET-005 - Fluxo Completo: Compra + Pagamento via API Mercado Pago
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    end-to-end    mercado-pago-api
    [Documentation]    Fluxo COMPLETO: evento → ticket type → pagamento via API
    
    ${response_compra}    ${ticket_type_id}=    Executar Fluxo Completo Pagamento    1
    Validar_Response_Compra_Sucesso    ${response_compra}
    ${response_json}=    Set Variable    ${response_compra.json()}

POST-BUY-TICKET-006 - Validar falha com ID de ticket type inexistente
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Falha ao usar ID de ticket type que não existe

    ${uuid_inexistente}=    Set Variable    12345678-1234-1234-1234-123456789012
    ${response}=    Executar_Compra_Ticket    ${uuid_inexistente}    1    404

POST-BUY-TICKET-007 - Validar falha por quantidade de ingressos excede disponibilidade
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Falha ao comprar mais tickets que a capacidade

    ${ticket_type_id}=    Criar_Ticket_Type_Com_Capacidade    1
    ${response}=    Executar_Compra_Ticket    ${ticket_type_id}    2    409

POST-BUY-TICKET-008 - Validar falha por titular duplicado no mesmo payload
    [Tags]    ${TAG_ALL_TESTS}    FALHA    TITULAR_DUPLICADO_PAYLOAD
    [Documentation]    Falha por mesmo titular aparece mais de uma vez no payload
    
    ${ticket_type_id}=    Criar Ticket Type E Capturar ID
    ${payload}=           Criar Payload Compra Com Titular Duplicado    ${ticket_type_id}
    ${response}=          Executar_Compra_Ticket_Com_Payload    ${payload}    422

POST-BUY-TICKET-009 - Validar falha por idade mínima não atendida
    [Tags]    ${TAG_ALL_TESTS}    FALHA    IDADE_MINIMA
    [Documentation]    Falha por titular não atende idade mínima do evento
    
    ${ticket_type_id}=    Criar Ticket Type Com Idade Minima
    ${payload}=           Criar Payload Compra Ticket Com Menor Idade    ${ticket_type_id}
    ${response}=          Executar_Compra_Ticket_Com_Payload    ${payload}    422

POST-BUY-TICKET-010 - Cenário de Falha: Ticket Esgotado com Retry
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}    end-to-end    esgotado
    [Documentation]    Versão com retry para garantir esgotamento
    
    ${event_id}=    Criar Evento Base Para Tickets
    ${ticket_name}=    Gerar Nome Ticket Aleatorio
    &{ticket_payload}=    Criar Payload Ticket Basico    ${ticket_name}    100.00    1
    ${response_ticket}=    Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    201
    ${ticket_type_id}=    Set Variable    ${response_ticket.json()['id']}
    ${response_compra_1}=    Executar Pagamento Mercado Pago Via API    ${ticket_type_id}    1
    Validar_Response_Compra_Sucesso    ${response_compra_1}
    FOR    ${attempt}    IN RANGE    1    6
        Sleep    5s
        
        ${card_token}=    Gerar Token Cartao Mercado Pago
        &{payload}=    Criar Payload Compra Com Token    ${ticket_type_id}    1    ${card_token}
        
        ${headers}=    Create Dictionary    Content-Type=application/json
        
        ${response}=    POST On Session    api_session    ${ENDPOINT_BUY_TICKET}
        ...    json=${payload}
        ...    headers=${headers}
        ...    expected_status=any
        
        Run Keyword If    ${response.status_code} == 422    Exit For Loop
    END
    Should Be Equal As Integers    ${response.status_code}    409
